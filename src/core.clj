(ns core
  (:gen-class)
  (:require
   [replies               :as r]
   [routes :as routes]
   [spoiler-channels      :as i]
   [clj-time       [core :as time]]
   [clojure.string        :as string]
   [clojure.core.async    :as async]
   [discljord.connections :as connection]
   [discljord.messaging   :as msg]
   [discljord.formatting :as fmt]
   [clojure.pprint :as pp]))

(def TEST
  true)

(def token
  "Discord bot token, used as ID and 'security measure' in one. Use your own!"
  (slurp "./data/discord_bot.key"))

(def guild-id
  "The guild you use to test the bot. Global commands can take a while to register in comparison.
  Note: There is a global rate limit of 200 application command creates per day, per guild"
  1369725817038442616)

(def app-id
  "Application ID of the bot."
  1369689799413334178)

;; (def intents #{:guilds :guild-messages})

;; (defn route-msg
;;   "Decides on what output message to generate based on the routes (condition->result) and the input message.
;;   Routes will be handled from front to back."
;;   [routes msg event]
;;   (let [route (first routes)]
;;     (when (= 0 (count routes)) (prn "No match!"))
;;     (if ((:condition route) msg event)
;;       ((:result route) msg event)
;;       (recur (rest routes) msg event))))

;; (def router
;;   "Message router, partially applied with all routes."
;;   (partial route-msg
;;            ;; Order matters here, as the router will go through this front to back.
;;            [routes/empty-mdn
;;             routes/empty-ns
;;             routes/version
;;             routes/too-long
;;             routes/robot
;;             routes/poast-coad
;;             routes/pspsps
;;             routes/zoe
;;             routes/persecution
;;             routes/duck
;;             routes/naughty
;;             routes/lookup-mdn
;;             routes/lookup-ns]))

;; (defn- event-enricher
;;   "Turns an event into a map with all relevant data."
;;   [event message-ch n]
;;   ;; Ensure bogus requests are ignored early.
;;   (when (< 70 (count (:content event))))

;;   (let [msg (-> event :content (string/replace #"(?i)^!(MDN|NS)\b" "") r/lcase-&-rm-ns)
;;         ;; Pass the event to the router
;;         reply (router msg event)]
;;     (m/create-message! message-ch (:channel-id event) :content reply)))

;; ;; Dev note: https://gist.github.com/chrischambers/fcbbb4d1e856fdab6fa173d713fa96e7

;; (defn -main
;;   "Start the server.
;;    Note to Zoë and other REPLers, use: `(doto (Thread. -main) (.setDaemon true) (.start))`."
;;   []
;;   (letfn [(check-prefix [data] (re-find #"(?i)^!(MDN|NS)\b" (get data :content "")))]
;;     (let [event-ch     (async/chan 100)
;;           _conn_ch     (c/connect-bot! token event-ch :intents intents)
;;           message-ch   (m/start-connection! token)]
;;       (try
;;         (loop [n 0]
;;           (recur
;;            (let [[type data] (async/<!! event-ch)
;;                  msg?        (= :message-create type)
;;                  notbot?     (-> data :author :bot not)
;;                  for-me?     (check-prefix data)
;;                  ok?         (and msg? notbot? for-me?)]
;;              (prn data)
;;              (if ok? (do (event-enricher data message-ch n) (inc n)) n))))

;;         (finally
;;           (m/stop-connection! message-ch)
;;           (async/close!           event-ch))))))


(def conn (msg/start-connection! token))
(def events (async/chan 100 (comp (filter (comp #{:interaction-create} first)) (map second))))
(def channel (connection/connect-bot! token events :intents #{}))


(def greet-cmd
  {:name "hello"

   :description "Say hi to someone"

   :options [{;; See this link for more info about the type: https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-option-type
              :type 6
              :name "user"
              :description "The user to greet"}]

   :handler (fn
              ;; `target-id` will be the user id of the user to greet (if set)
              [{:keys [id token] {{user-id :id} :user} :member {[{target-id :value}] :options} :data}]
              (msg/create-interaction-response! conn id token 4 :data {:content (str "Hello, " (fmt/mention-user (or target-id user-id)) " :smile:")}))})

(def ns-cmd
  {:name "ns"

   :description "Look up documentation of an NS API entry."

   :options [{;; See this link for more info about the type: https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-option-type
              :type 3
              :name "query"
              :description "What you want to look for in the NS API."}]

   :handler (fn
              [{:keys [id token] {cmd-options :options} :data}]
              (msg/create-interaction-response! conn id token 4 :data {:content
                                                                       (str "I haven't connected NS search yet, but here's the query: " (:value (first cmd-options)) )}))})

(defn register-command!
  "Register a single command."
  [command]
  ;; Params: guild id (omit for global commands), command name, command description, optionally command options
  (println @(msg/create-guild-application-command! conn app-id guild-id (command :name) (command :description) :options (command :options)))

  (when (not TEST)
    @(msg/create-global-application-command! conn app-id (command :name) (command :description) :options (command :options))))

(defn register-commands!
  []
  (let [commands [ns-cmd greet-cmd]]
    ;; We're not using msg/bulk-overwrite-guild-application-commands! here, due to the internals for making commands not being exposed
    (run! register-command! commands))
  )

(defn handle-command
  ;; `target-id` will be the user id of the user to greet (if set)
  [interaction]

  ;; For testing/debugging
  (pp/pprint interaction)

  ;; Destructure interaction for easy access
  (let [{:keys [id token] {cmd-name :name cmd-options :options} :data} interaction]
    (println cmd-name)
    (println cmd-options)

    (if (= "ns" cmd-name)
      ((ns-cmd :handler) interaction)
      ((greet-cmd :handler) interaction)
      ;;(:name (first cmd-options))
      )
    (msg/create-interaction-response! conn id token 4 :data {:content (str "Hello, " (fmt/mention-user "zonoia") " :smile:")})))

(defn -main
  "Register and handle commands."
  []
  (register-commands!)

  (async/go-loop []
    (when-let [interaction (async/<! events)]
      ;; See example interaction: https://discord.com/developers/docs/interactions/application-commands#slash-commands-example-interaction
      (handle-command interaction)
      (recur)))
  )

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
   [discljord.formatting :as fmt]))

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


(def greet
  {:name "hello"

   :description "Say hi to someone"

   :options [{:type 6 ; The type of the option. In this case, 6 - user. See the link to the docs above for all types.
              :name "user"
              :description "The user to greet"}]

   :handler (fn
              ;; `target-id` will be the user id of the user to greet (if set)
              [{:keys [id token] {{user-id :id} :user} :member {[{target-id :value}] :options} :data}]
              (msg/create-interaction-response! conn id token 4 :data {:content (str "Hello, " (fmt/mention-user (or target-id user-id)) " :smile:")}))})

(defn register-commands
  []
  ;; Params: guild id (omit for global commands), command name, command description, optionally command options
  @(msg/create-guild-application-command! conn app-id guild-id (greet :name) (greet :description) :options (greet :options))
  ;;(msg/bulk-overwrite-guild-application-commands! conn app-id guild-id commands & {:as opts, :keys [:user-agent :audit-reason]})
  )

(defn handle-command
  ;; `target-id` will be the user id of the user to greet (if set)
  [{:keys [id token] {{user-id :id} :user} :member {[{target-id :value}] :options} :data}]
  (msg/create-interaction-response! conn id token 4 :data {:content (str "Hello, " (fmt/mention-user (or target-id user-id)) " :smile:")}))

(defn -main
  "Register and handle commands."
  []
  (register-commands)

  (async/go-loop []
    (when-let [interaction (async/<! events)]
      ;; See example interaction: https://discord.com/developers/docs/interactions/application-commands#slash-commands-example-interaction
      (handle-command interaction)
      (recur)))
  )

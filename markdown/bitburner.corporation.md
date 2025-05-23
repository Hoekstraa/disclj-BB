<!-- Do not edit this file. It is automatically generated by API Documenter. -->

[Home](./index.md) &gt; [bitburner](./bitburner.md) &gt; [Corporation](./bitburner.corporation.md)

## Corporation interface

Corporation API

**Signature:**

```typescript
export interface Corporation extends WarehouseAPI, OfficeAPI 
```
**Extends:** [WarehouseAPI](./bitburner.warehouseapi.md)<!-- -->, [OfficeAPI](./bitburner.officeapi.md)

## Methods

|  Method | Description |
|  --- | --- |
|  [acceptInvestmentOffer()](./bitburner.corporation.acceptinvestmentoffer.md) | Accept the investment offer. The value of offer is based on current corporation valuation. |
|  [bribe(factionName, amountCash)](./bitburner.corporation.bribe.md) | <p>Bribe a faction. You must satisfy these conditions:</p><p>- The corporation valuation must be greater than or equal to a threshold. You can use [getCorporation](./bitburner.corporation.getcorporation.md) and [getConstants](./bitburner.corporation.getconstants.md) to get this information.</p><p>- You must be a member of the specified faction.</p><p>- The specified faction must offer at least 1 type of work. You can use [getFactionWorkTypes](./bitburner.singularity.getfactionworktypes.md) to get the list of work types of a faction.</p> |
|  [buyBackShares(amount)](./bitburner.corporation.buybackshares.md) | Buyback shares. Spend money from the player's wallet to transfer shares from public traders to the CEO. |
|  [canCreateCorporation(selfFund)](./bitburner.corporation.cancreatecorporation.md) | Return whether the player can create a corporation. Does not require API access. |
|  [createCorporation(corporationName, selfFund)](./bitburner.corporation.createcorporation.md) | <p>Create a Corporation. You should use [canCreateCorporation](./bitburner.corporation.cancreatecorporation.md) to check if you are unsure you can do it, because it throws an error in these cases:</p><p>- Use seed money outside BitNode 3.</p><p>- Be in a BitNode that has CorporationSoftcap (a BitNode modifier) less than 0.15.</p> |
|  [expandCity(divisionName, city)](./bitburner.corporation.expandcity.md) | Expand to a new city. |
|  [expandIndustry(industryType, divisionName)](./bitburner.corporation.expandindustry.md) | Expand to a new industry. |
|  [getBonusTime()](./bitburner.corporation.getbonustime.md) | Get bonus time. Bonus time is accumulated when the game is offline or if the game is inactive in the browser. Bonus time makes the corporation progress faster. |
|  [getConstants()](./bitburner.corporation.getconstants.md) | Get corporation-related constants. |
|  [getCorporation()](./bitburner.corporation.getcorporation.md) | Get corporation data. |
|  [getDivision(divisionName)](./bitburner.corporation.getdivision.md) | Get division data. |
|  [getIndustryData(industryName)](./bitburner.corporation.getindustrydata.md) | Get constant data of an industry. |
|  [getInvestmentOffer()](./bitburner.corporation.getinvestmentoffer.md) | Get an offer for investment based on current corporation valuation. |
|  [getMaterialData(materialName)](./bitburner.corporation.getmaterialdata.md) | Get constant data of a material. |
|  [getUnlockCost(upgradeName)](./bitburner.corporation.getunlockcost.md) | Get the cost to unlock a one-time unlockable upgrade. |
|  [getUpgradeLevel(upgradeName)](./bitburner.corporation.getupgradelevel.md) | Get the level of a levelable upgrade. |
|  [getUpgradeLevelCost(upgradeName)](./bitburner.corporation.getupgradelevelcost.md) | Get the cost to unlock the next level of a levelable upgrade. |
|  [goPublic(numShares)](./bitburner.corporation.gopublic.md) | Go public. |
|  [hasCorporation()](./bitburner.corporation.hascorporation.md) | Return whether the player has a corporation. Does not require API access. |
|  [hasUnlock(upgradeName)](./bitburner.corporation.hasunlock.md) | Check if you have a one-time unlockable upgrade. |
|  [issueDividends(rate)](./bitburner.corporation.issuedividends.md) | Issue dividends. |
|  [issueNewShares(amount)](./bitburner.corporation.issuenewshares.md) | Issue new shares. |
|  [levelUpgrade(upgradeName)](./bitburner.corporation.levelupgrade.md) | Level up an upgrade. |
|  [nextUpdate()](./bitburner.corporation.nextupdate.md) | Sleep until the next Corporation update happens. |
|  [purchaseUnlock(upgradeName)](./bitburner.corporation.purchaseunlock.md) | Unlock an upgrade. |
|  [sellDivision(divisionName)](./bitburner.corporation.selldivision.md) | Sell a division. |
|  [sellShares(amount)](./bitburner.corporation.sellshares.md) | Sell shares. Transfer shares from the CEO to public traders to receive money in the player's wallet. |


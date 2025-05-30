<!-- Do not edit this file. It is automatically generated by API Documenter. -->

[Home](./index.md) &gt; [bitburner](./bitburner.md) &gt; [Grafting](./bitburner.grafting.md)

## Grafting interface

Grafting API

**Signature:**

```typescript
export interface Grafting 
```

## Remarks

This API requires Source-File 10 to use.

## Methods

|  Method | Description |
|  --- | --- |
|  [getAugmentationGraftPrice(augName)](./bitburner.grafting.getaugmentationgraftprice.md) | Retrieve the grafting cost of an aug. |
|  [getAugmentationGraftTime(augName)](./bitburner.grafting.getaugmentationgrafttime.md) | Retrieves the time required to graft an aug. Do not use this value to determine when the ongoing grafting finishes. The ongoing grafting is affected by current intelligence level and focus bonus. You should use [waitForOngoingGrafting](./bitburner.grafting.waitforongoinggrafting.md) for that purpose. |
|  [getGraftableAugmentations()](./bitburner.grafting.getgraftableaugmentations.md) | Retrieves a list of augmentations that can be grafted. |
|  [graftAugmentation(augName, focus)](./bitburner.grafting.graftaugmentation.md) | Begins grafting the named aug. You must be in New Tokyo to use this. When you call this API, the current work (grafting or other actions) will be canceled. |
|  [waitForOngoingGrafting()](./bitburner.grafting.waitforongoinggrafting.md) | Wait until the ongoing grafting finishes or is canceled. |


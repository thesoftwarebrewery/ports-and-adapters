# What
A demo application service that illustrates `an approach` one could take in implementing ports and adapters.

# Idea
This hypothetical software `application service` is a deployable software component.
It's job is to mediate between two `business domains`:
1) the `offers` `business domain`, to which this `application service` belongs
2) the `promos` `business domain`, to which this `application service` listens

# Context
![context diagram](/docs/offer-promo-diagram.drawio.png)

# Terminology
| term | definition|
| --  | -- |
| application service | a deployable software component |
| business domain | a bounded set of ubiqitous concepts / ideas that exist to communicate and reason about activities in that context |
| application domain | a bounded set of ubitious concepts ideas / that exist to solve activities within that application context |

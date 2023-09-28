# What
A demo application service that illustrates `an approach` one could take in implementing ports and adapters.

![ports and adapters](/docs/ports-and-adapters.drawio.png)

# Resources
* [Recording of presentation for this code sample](https://www.youtube.com/watch?v=yGBrUkFgIF4) (Dutch)

# Development
Start application service dependencies (pubsub, postgres) as configured by `docker-compose`.
```shell
make environment-up
```

Build and run test suite
```shell
make test
```

# Idea
This hypothetical software `application service` is a deployable software component, written in an architecturally
evident code style as ports and adapters.

Its job is to mediate between two `business domains`:
1) the `offers` `business domain`, to which this `application service` belongs
2) the `promos` `business domain`, to which this `application service` listens

## System context
![context diagram](/docs/offer-promo-diagram.drawio.png)

## Terminology
| term                | definition                                                                                                        |
|---------------------|-------------------------------------------------------------------------------------------------------------------|
| application service | a deployable software component                                                                                   |
| business domain     | a bounded set of ubiqitous concepts / ideas that exist to communicate and reason about activities in that context |
| application domain  | a bounded set of ubitious concepts ideas / that exist to solve activities within that application context         |

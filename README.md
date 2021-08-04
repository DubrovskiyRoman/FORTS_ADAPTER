
# Trader's Office 

Platform for obtaining market data from the Plaza-2 gateway derivatives market 
section of the Moscow Exchange.

It has a microservice architecture and consists of the following services:

#1 FORTS_ADAPTER https://github.com/DubrovskiyRoman/FORTS_ADAPTER.git

Connects to the Plaza-2 gateway to receive the necessary data.

#2 FORTS_BACK-OFFICE https://github.com/DubrovskiyRoman/FORTS_BACK-OFFICE.git

Receives information from the FORTS_ADAPTER service for the back office 
(instruments, orders, transactions).

#3 FORTS_MARKET-DATA https://github.com/DubrovskiyRoman/FORTS_MARKET_DATA.git 

Receives market data (ticks, usd rates) from the FORTS_ADAPTER service.

#4 FORTS_REST_API_STATISTICS https://github.com/DubrovskiyRoman/FORTS_REST_API_STATISTICS.git 

Provides general statistics on working with the PLAZA-2 gateway.

#5 FORTS_REST_API_BACK-OFFICE https://github.com/DubrovskiyRoman/FORTS_REST_API_BACK-OFFICE.git 

Provides data from the back office.
## Tech Stack

**Server:** Spring (Framework, Boot, JDBC, Data, MVC), Java 8, Maven, PostgreSQL, MySQL, 
MongoDB, Docker, Kubernetes

**Patterns:** Bridge, builder, chain of responsobility, SAGA (in the process of implementation), CQRS   


  
## Stratistic API Reference (for local launch)

#### Actual statistics on received message during the day

```http
  GET /localhost:8080/v1/PLAZA-2/total-statistics/actual
```


#### General information on all gateways and exchanges

```http
  GET /localhost:8080/v1/trader-office/stocks-gateways
```

## Back Office API Reference

#### Information for instrument by code

```http
  GET /localhost:8080/v1/forts/back-office/instruments/instrumentCode={instrumentCode}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `instrumentCode`      | `String` | **Required**. Code of Instrument to fetch |


Takes instrument code and returns the whole information on him.

#### Information for instrument by ISIN

```http
  GET /localhost:8080/v1/forts/back-office/instruments/instrumentISIN={instrumentIsin}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `instrumentIsin`      | `String` | **Required**. ISIN of Instrument to fetch |


Takes instrument ISIN and returns the whole information on him.
  
#### Actual information for all instruments for the current day 

```http
  GET /localhost:8080/v1/forts/back-office/instruments/actual-instruments
```

#### Actual information for all orders for the current day 

```http
  GET /localhost:8080/v1/forts/back-office/orders/actual
```
#### Information for orders by client code

```http
  GET/localhost:8080/v1/forts/back-office/orders/client-code={clientCode}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `clientCode`      | `String` | **Required**. Code of the client in order. |


Takes client code and returns all orders for him.

#### Actual information about orders wiht deals 

```http
  GET /localhost:8080/v1/forts/back-office/orders/orders-with-deals
```
## Documentation

[Documentation](https://drive.google.com/file/d/19I5sMjEUiBymxIMHSmrtDclbLl1KIqcP/view?usp=sharing)

  

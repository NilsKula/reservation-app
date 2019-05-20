#Reservation App

Simple Reservation app. It supports two types of users - hotel employees and customers.
Employees from internal-api are able to manage rooms, add/update/remove. See schedules for each room
and see statistics for available rooms for specified period. Customers are able to view available rooms 
for specific period and make reservations.

To start application locally, use docker and enter command in terminal
`docker run -p 5432:5432 -e POSTGRES_USER=nils -e POSTGRES_PASSWORD=nils -e POSTGRES_DB=reservation_app postgres`

## Internal API

**Fetch all rooms**
*Request:*

```GET http://localhost:8080/internal-api/rooms```

*Response:*

Status code: ```200 OK```

Response body:
```json

[
    {
        "roomNumber": 405,
        "price": 100.2,
        "roomSize": 4
    }
]

```

**Manage rooms**
*Request:*

```PUT http://localhost:8080/internal-api/rooms/manage```

Status code: ```200 OK```

Request body:
```json
{
	"roomNumber": 405,
	"price": 100.2,
	"roomSize": 4
}
```

**View schedules for all rooms**
*Request:*

```GET http://localhost:8080/internal-api/rooms/schedule```

Status code: ```200 OK```

Response body:
```json
[
    {
        "roomNumber": 405,
        "reservations": [
            {
                "checkIn": "2018-03-20",
                "checkOut": "2019-03-21"
            },
            {
                "checkIn": "2019-04-10",
                "checkOut": "2019-04-21"
            }
        ]
    }
]
```

**Check availability statistics for period**
*Request:*

```GET http://localhost:8080/internal-api/rooms/availability```

Status code: ```200 OK```

Where query parameters are:

   - *from* - day from
   - *to* - day to
   
*Response:*

Response body:
```json
{
    "roomsFree": 1,
    "roomsBusy": 1
}
```

**Remove room**
*Request:*

```DELETE http://localhost:8080/internal-api/rooms/{room-number}```

*Response*

Status code: ```200 OK```

##Public API

**Reserve room**
*Request:*

```POST http://localhost:8080/public-api/rooms/{room-number}/reserve```

Request body:
```json
{
	"checkIn":"2019-04-10",
	"checkOut":"2019-04-21"
}
```

*Response:*

Status code: ```200 OK```

**View available rooms**
*Request:*

```GET http://localhost:8080/public-api/rooms/search```

Where query parameters are:

   - *checkIn* - checkin day
   - *checkOut* - checkout day

*Response*

Status code: ```200 OK```

Response body:
```json

[
    {
        "roomNumber": 425,
        "price": 100.2,
        "roomSize": 4
    }
]
```



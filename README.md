<p align="center">
  <img src="./.github/noun-piggy-bank-131970.svg" alt="" width="128" height="128">
</p>

<h1 align="center">Piggy Bank for Words</h1>

## Backend

The backend uses Spring Boot.

Run using

```
> cd backend
> mvn spring-boot:run
```

### API

#### GET /api/dashboard

##### Response

```
{
  "errorMessage": <string>,
  "members": [
    {
      "id": <number>,
      "name": <string>,
      "amountCalculated": <number>,
      "amountAdjusted": <number>
    }, ...
  ]
}
```

#### POST /api/penalty

###### Request

```
{
    "memberName": <string>,
    "amount": <number>
}
```

###### Response

```
{
    "errorMessage": <string>,
    "memberId": <number>,
    "memberName": <string>,
    "amount": <number>,
    "calculatedTotal": <number>
}
```

#### DELETE /api/penalty/{id}

###### Response

```
{
    "errorMessage": <string>
}
```

#### PATCH /api/penalty

###### Request

```
{
    "id": <number>,
    "amount": <number>
}
```

###### Response

```
{
    "errorMessage": <string>
    "id": <number>,
    "amount": <number>
}
```

##### PATCH /api/penalty_total

###### Request

```
{
    "memberId": <number>,
    "amount": <number>
}
```

###### Response

```
{
    "errorMessage": <string>,
    "memberId": <number>,
    "amount": <number>
}
```

##### GET /api/team_member/{id}

###### Response

```
{
  "errorMessage": <string>,
  "id": <number>,
  "name": <string>,
  "amountCalculated": <number>,
  "amountAdjusted": <number>,
  "penalties": [
    {
        "id": <number>,
        "dateTime": <datetime>,
        "amount": <number>
    }, ...
  ] 
}
```


## Frontend

The frontend uses Vue 3 and Vite.

Run using

```
> cd frontend
> npm run dev
```

In your browser, navigate to 

```
http://localhost:5173
```

NOTE: existing typing issues have not been resolved yet.
So building this with `npm run build` will fail. 

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
    "memberId": <number>,
    "memberName": <string>,
    "amount": <number>,
    "calculatedTotal": <number>
}
```

##### POST /api/penalty_total

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
    "memberId": <number>,
    "amount": <number>
}
```

##### GET /api/team_member/{id}

###### Response

```
{
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
> npm run preview
```

In your browser, navigate to 

```
http://localhost:4173
```

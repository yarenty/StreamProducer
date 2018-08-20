Stream-Er => Stream Producer
----------------------------


# Intro

Utility tool that will send stream of data to specific URL from specific source with configured interval.


## Use Case 1
Read data from CSV file, and send line by line as POST to specific address

### Example
Send every 5 sec:
Intput:
```
10/08/2018,1,2,99,cool
11/08/2018,4,3,78,be
12/08/2018,7,0,89,stuff
```

Output:
```
POST | URL | "10/08/2018,1,2,99,cool"
```
5 sec later:
```
POST | URL | "11/08/2018,4,3,78,be"
```
5 sec later:
```
POST | URL | "12/08/2018,7,0,89,stuff"
```


## Use Case 2
1. Read data from CSV file with headers
2. Send line by line with automatic transformation as GET to specific address

### Example
Send every 5 sec:
Intput:
```
time,event_type,count,sensor,comment
10/08/2018,1,2,99,cool
11/08/2018,4,3,78,be
12/08/2018,7,0,89,stuff
```
Output:
```
GET | URL | ?time=10/08/2018&event_type=1&count=2&sensor=99&comment=cool
```
5 sec later:
```
GET | URL | ?time=11/08/2018&event_type=4&count=3&sensor=78&comment=be
```


## Use Case 3
1. Read data from CSV file with headers
2. Send line by line with automatic transformation as REST 

*Example same as above..*
 

## Use Case 4
1. Read data from CSV file with headers
2. Send line by line with automatic transformation as REST JSON simple structure



### Example
Send every 5 sec:
Intput:
```
time,event_type,count,sensor,comment
10/08/2018,1,2,99,cool
11/08/2018,4,3,78,be
12/08/2018,7,0,89,stuff
```
Output:
```
POST | URL | 
{
    time:"10/08/2018",
    event_type:1,
    count:2,
    sensor:99,
    comment:"cool"
}
```
5 sec later:
```
POST | URL | 
{
    time:"11/08/2018",
    event_type:4,
    count:3,
    sensor:78,
    comment:"be"
}
```



# Usage

```
-in simple_with_header.csv -url http://localhost:9200

```



# Changelog

## v 0.01 
- initial version
- example use cases
- environment configuration



(C)2018 by yarenty
# Weather extension for [Glance](https://github.com/glanceapp/glance)

This extension is similar in functionality to the built-in weather widget,
but the data is retrieved from local providers, thus more precise.

## Usage
I can't bother publishing the image, you have to build it first
```
1. clone the repository
2. docker build --tag=<whatever> .
```

Docker Compose example:
```yaml
services:
  weather-ext:
    image: <your image name>
    container_name: weather-ext
    restart: unless-stopped
    environment:
      WEATHER_ENDPOINT: # you have to know the weather API endpoint. I am not providing it to avoid legal problems.
      CITY: # the city in lowercase
      LATITUDE: # your approx. coordinates, used for daylight calculations
      LONGITUDE: # ^^
      TIMEZONE: # your timezone e.g. Europe/London
```

Now you can add the widget to your `glance.yml`:
```yaml
- type: extension
  url: <your host>:8080
  allow-potentially-dangerous-html: true
  cache: 30s # adjust this as you want, don't go low, you might get restricted for api abuse
```
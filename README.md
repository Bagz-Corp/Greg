# GREG

**Greg** is an aggregator for search in different common 3D models databases. 
This is the very first version of the app.

# Architecture
I'll try to follow an MVVM architecture along the whole project.

### Networking
**Greg** is using Retrofit for network request.

The goal of **Greg** is to fetch data from different online db using public APIs. While adding MakerWorld, a choice has been made to have a separate Retrofit Service for each. \
Also, our network layer returns two methods allowing to fetch separately the data from each endpoint. The Repository sends those data to the database layer. \
The architectural choice here was to store inside one table the 2 results set of data in order to ease the reception of those data, but with the inconvenience of the coupling of the data. Another inconvenience is that this avoid the "waterfall-effect" we could expect in an UI point-of-view. 

Another choice would have to keep separated the set of data coming from different endpoint, and have an independant display. But adding an endpoint would bring scaling duplication and complexity, while the current choice limit this a little bit. 

A better architecture could be imagined and documented in the future.

### Dependency Injection
**Greg** is using Hilt for DI

## Where does Greg looks ? 
**Greg** is looking at several online database. I'll provide information which helped me fetch data here when it's available online. 

### SketchLab
**SketchLab** has a very well documented Swagger publicly available [here](https://docs.sketchfab.com/data-api/v3/index.html#!/search/get_v3_search_type_models)

### MakerWorld
**MakerWorld** doesn't document very well its API. It has been decided to use the "https://makerworld.com/api/v1/search-service/select/design2" path. At least for the moment when it works.
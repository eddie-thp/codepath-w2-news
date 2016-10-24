# Project 2 - *Old News*

**Old News** is an android app that allows a user to search for articles on web using simple filters. The app utilizes [New York Times Search API](http://developer.nytimes.com/docs/read/article_search_api_v2).

Time spent: **14+** hours spent in total


**IMPORTANT:**
In order to use the application, developer needs to create a secrets.xml file accordingly to the["Secrets in resource files" codepath.com guide](http://guides.codepath.com/android/Storing-Secret-Keys-in-Android#secrets-in-resource-files) and store the following keys:

* **api_key_ny_times** : [The New York Times API](http://developer.nytimes.com/) Key

**secrets.xml** sample:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="api_key_ny_times"> </string>
</resources>
```


## User Stories

The following **required** functionality is completed:

* [x] User can **search for news article** by specifying a query and launching a search. Search displays a grid of image results from the New York Times Search API.
* [x] User can click on "settings" which allows selection of **advanced search options** to filter results
* [x] User can configure advanced search filters such as:
  * [x] Begin Date (using a date picker)
  * [x] News desk values (Arts, Fashion & Style, Sports)
  * [x] Sort order (oldest or newest)
* [x] Subsequent searches have any filters applied to the search results
* [x] User can tap on any image in results to see the full text of article **full-screen**
* [x] User can **scroll down to see more articles**. The maximum number of articles is limited by the API search.

The following **optional** features are implemented:

* [ ] Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
* [x] Used the **ActionBar SearchView** or custom layout as the query box instead of an EditText
* [x] User can **share an article link** to their friends or email it to themselves
* [x] Replaced Filter Settings Activity with a lightweight modal overlay
* [ ] Improved the user interface and experiment with image assets and/or styling and coloring

The following **bonus** features are implemented:

* [x] Use the [RecyclerView](http://guides.codepath.com/android/Using-the-RecyclerView) with the `StaggeredGridLayoutManager` to display improve the grid of image results
* [ ] For different news articles that only have text or only have images, use [Heterogenous Layouts](http://guides.codepath.com/android/Heterogenous-Layouts-inside-RecyclerView) with RecyclerView
* [x] Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.com/android/Using-Parceler).
* [x] Leverages the [data binding support module](http://guides.codepath.com/android/Applying-Data-Binding-for-Views) to bind data into layout templates.
* [ ] Replace all icon drawables and other static image assets with [vector drawables](http://guides.codepath.com/android/Drawables#vector-drawables) where appropriate.
* [x] Replace Picasso with [Glide](http://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en) for more efficient image rendering.
* [ ] Uses [retrolambda expressions](http://guides.codepath.com/android/Lambda-Expressions) to cleanup event handling blocks.
* [x] Leverages the popular [GSON library](http://guides.codepath.com/android/Using-Android-Async-Http-Client#decoding-with-gson-library) to streamline the parsing of JSON data.
* [x] Leverages the [Retrofit networking library](http://guides.codepath.com/android/Consuming-APIs-with-Retrofit) to access the New York Times API.
* [ ] Replace the embedded `WebView` with [Chrome Custom Tabs](http://guides.codepath.com/android/Chrome-Custom-Tabs) using a custom action button for sharing. (_**2 points**_)

The following **additional** features are implemented:

* [ ] List anything else that you can get done to improve the app functionality!

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/link/to/your/gif/file.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app:

* **RecyclerView**: Missed setting up the layout manager, items were not being rendered
* **DataBinding**: It was tricky working with it
    * Binding classes, which are created based on the data tag, would only be generated after rebuilding the app
    * Same with cases where I had to use the BindAdapter annotation, which drives the XML app tag
    * I may be missing 2 way binding, I still need to improve my settings fragment so that I can better use data-binding with the settings fragment.
* **Glide** I had a bit of trouble with glide, maybe because I am using a DynamicHeightImageView, for some reason images wouldn't be rendered at all, I finally figure out how to use a SimpleTarget to do it. But I do not know if it's the most correct way of implementing it.
* **Gson** While modifying the model I forgot to make other/new models parcelable with the use of the @Parcels annotation, that caused problems to the data-binding framework - it stopped generating the binding classes

## Open-source libraries used

- [Retrofit](https://github.com/square/retrofit) - Type-safe HTTP client for Android and Java by Square, Inc.
- [Gson](https://github.com/google/gson) - A Java serialization/deserialization library that can convert Java Objects into JSON and back.
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android
- [Glide Transformations](https://github.com/wasabeef/glide-transformations) - An Android transformation library providing a variety of image transformations for Glide.
- [AndroidStaggeredGrid](https://github.com/etsy/AndroidStaggeredGrid) - An Android staggered grid view which supports multiple columns with rows of varying sizes.

## License

    Copyright 2016 Eduardo Theiss Przysiezny

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
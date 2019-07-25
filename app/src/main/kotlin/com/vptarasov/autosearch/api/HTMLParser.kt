package com.vptarasov.autosearch.api

import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.model.*
import com.vptarasov.autosearch.util.Constants
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.*

class HTMLParser {


    fun getNewsPreviews(html: String): ArrayList<News> {
        val newsPreviews = ArrayList<News>()
        val element = Jsoup.parse(html).getElementsByClass("green_content")
        try {
            for (i in 0 until element.select("a").size) {
                val newsPreview = News()
                newsPreview.title = element.select("a")[i].text()
                newsPreview.text = element.select("div[style=\"margin-top:5px;\"]")[i].text()
                newsPreview.url = element.select("a")[i].attr("href")
                newsPreview.photo = Constants.NEWS_URL + element.select("img")[i].attr("src")
                newsPreview.id = newsPreview.run { url?.replace("/", "") }!!.replace(".", "").replace("-", "").replace("_", "")
                newsPreviews.add(newsPreview)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return newsPreviews
    }

    fun getNews(html: String): News {
        val news = News()
        val element = Jsoup.parse(html).select("td.content")
        try {
            news.title = element.select("h1").text()
            val content = element.select("div.green_content")
            news.text = content.toString().replace("href".toRegex(), "").replace("Источник: ".toRegex(), "")
                .replace("http://www.autonews.ru".toRegex(), "")

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return news
    }

    fun getCarList(html: String): ArrayList<Car> {
        val carsLists = ArrayList<Car>()
        val element = Jsoup.parse(html).select("div[id=results]")
        var h: Element
        var line: Element
        var arrayTech: Array<String>
        var arrayRegio: Array<String>
        try {
            for (i in 0 until element.select("div[class^=car]").size) {
                val carsList = Car()
                carsList.photo = Constants.IMG_URL + element.select("img[id*=img]")[i].attr("src")
                carsList.city = element.select("a.green")[i].text()

                h = element.select("div[class=h]")[i]
                carsList.name = h.select("a").first().text()
                carsList.price =
                    h.getElementsByClass("tips").text().substring(h.getElementsByClass("tips").text().indexOf("/") + 2)
                carsList.year = h.select("strong").text()

                arrayTech = getValue(element.select("div[class=tech]")[i])
                carsList.engine = arrayTech[0]
                carsList.mileage = arrayTech[1]
                carsList.drive = arrayTech[2]
                carsList.gearbox = arrayTech[3]

                arrayRegio = getValue(element.select("div[class=regio]")[i])
                carsList.color = arrayRegio[1]
                carsList.body = arrayRegio[2]

                line = element.select("div[class=line]")[i]
                carsList.url = line.select("a").attr("href")
                carsList.date = line.select("span").text()

                carsLists.add(carsList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return carsLists
    }

    private fun getValue(element: Element): Array<String> {
        val text = element.html().replace("\n".toRegex(), "").replace("<strong>".toRegex(), "")
            .replace("</strong>".toRegex(), " ").replace("<span>".toRegex(), "").replace("</span>".toRegex(), " ")
        return text.split("<br>".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }

    fun getCar(html: String): Car {
        val car = Car()


        try {
            val element = Jsoup.parse(html).select("div[id=maincol]")
            val infocol = element.select("div[id=infocol]")

            car.name = infocol.select("h1").first().text()
            car.year = infocol.select("span").first().text()
            car.price = infocol.select("abbr").first().text()
            if (infocol.select("p").first() != null) {
                car.webMainText = infocol.select("p").first().html().replace(
                    "<span>Внимание! Не звоните на номера с кодами 090, 080, 070 <a href=\"//avtobazar.infocar.ua/faq/moshen/\" rel=\"nofollow\" class=\"red\">почему\\?</a></span>".toRegex(),
                    ""
                )
            } else {
                car.webMainText = ""

            }
            car.date = infocol.select("table").select("td.grey").html()

            val table = infocol.select("table")[1]
            car.engine = table.select("tr").first().text()
            car.mileage = App.instance?.getString(R.string.mileage) + infocol.select("strong").first().text()
            car.color = table.select("tr")[4].text()
            car.gearbox = table.select("tr")[1].text()
            car.drive = table.select("tr")[2].text()
            car.body = table.select("tr")[3].text()


            val fotocol = element.select("div[id=fotocol]")
            val city = fotocol.select("div[id=where]").select("a").first().text()
            car.city = if (city.contains("(")) city.substring(0, city.indexOf('(')) else city
            car.phone = Constants.IMG_URL + fotocol.select("img.phone").attr("src")
            car.photoSeller = Constants.IMG_URL + fotocol.select("img").attr("src")
            car.photo = Constants.IMG_URL + fotocol.select("div#big_foto.spinner2").select("a").attr("href")
            car.photoList = getPhotoList(element)


        } catch (e: Exception) {
            e.printStackTrace()
        }

        return car
    }

    private fun getPhotoList(el: Elements): ArrayList<String> {
        val photoList = ArrayList<String>()
        if (el.select("div#fotos") != null) {
            val foto = el.select("div[id=fotos]")
            if (el.select("div#fotos").size > 0) {

                for (i in 0 until foto.select("a").size) {
                    photoList.add(Constants.IMG_URL + foto.select("a")[i].attr("href"))
                }
            }
        }
        return photoList
    }

    fun getSearchData(html: String): SearchData {
        val searchData = SearchData()
        val elements = Jsoup.parse(html).select("div[id=maincol]")
        val element = elements.select("div[id=left_col]")
        searchData.body = getHashMap(element.select("select.kuzov").select("option"))
        searchData.color = getHashMap(element.select("select.color").select("option"))
        searchData.drive = getHashMap(element.select("select#pr").select("option"))
        searchData.mark = getHashMap(element.select("select.marksel").select("option"))
        searchData.region = getHashMap(element.select("select.regionsel").select("option"))
        searchData.engine = getHashMap(element.select("select#m1.twice").select("option"))
        searchData.year = getHashMap(element.select("select#y1.twice").select("option"))
        return searchData
    }

    fun getModel(html: String): Model {
        val model = Model()
        val elements = Jsoup.parse(html).select("div[id=maincol]")
        val element = elements.select("div[id=left_col]")
        model.model = (getHashMap(element.select("select.modelsel").select("option")))
        return model
    }

    fun getCity(html: String): City {
        val city = City()
        val elements = Jsoup.parse(html).select("div[id=maincol]")
        val element = elements.select("div[id=left_col]")
        city.city = (getHashMap(element.select("select.citysel").select("option")))
        return city
    }

    fun getLastPage(html: String): Int{
        val element = Jsoup.parse(html).select("div[id=paging]")
        return if(element.size > 0) Integer.valueOf(element.select("a").last().text()) else 1
    }


    private fun getHashMap(elements: Elements): Map<String, String> {
        val hashMap = HashMap<String, String>()
        for (i in elements.indices) {
            hashMap[elements.select("option")[i].attr("value")] = elements.select("option")[i].text()
        }
        return hashMap
    }
}


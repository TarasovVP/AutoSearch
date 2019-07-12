package com.vptarasov.autosearch.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.api.Api
import com.vptarasov.autosearch.model.City
import com.vptarasov.autosearch.model.Model
import com.vptarasov.autosearch.model.QueryDetails
import com.vptarasov.autosearch.model.SearchData
import com.vptarasov.autosearch.parser.HTMLParser
import com.vptarasov.autosearch.util.FragmentUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search_main.view.*
import java.util.*

class SearchFragment : BaseFragment() {

    private var searchData: SearchData? = null
    private var queryDetails: QueryDetails? = null
    private var model: Model? = null
    private var city: City? = null

    private var searchMark: Spinner? = null
    private var searchModel: Spinner? = null
    private var searchRegion: Spinner? = null
    private var searchCity: Spinner? = null
    private var searchYearFrom: Spinner? = null
    private var searchYearTo: Spinner? = null
    private var searchBody: Spinner? = null
    private var searchEngineUnit: Spinner? = null
    private var searchEngineFrom: Spinner? = null
    private var searchEngineTo: Spinner? = null
    private var searchColor: Spinner? = null
    private var searchPriceFrom: EditText? = null
    private var searchPriceTo: EditText? = null
    private var searchGearboxMech: CheckBox? = null
    private var searchGearboxAutom: CheckBox? = null
    private var searchPetrol: CheckBox? = null
    private var searchDiesel: CheckBox? = null
    private var searchGasPetrol: CheckBox? = null
    private var searchElectro: CheckBox? = null
    private var searchGas: CheckBox? = null
    private var searchPetrolElectro: CheckBox? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflateWithLoadingIndicator(R.layout.fragment_search_main, container)
        searchModel = view.searchModel
        searchCity = view.searchCity
        searchMark = view.searchMark
        searchRegion = view.searchRegion
        searchYearFrom = view.searchYearFrom
        searchYearTo = view.searchYearTo
        searchBody = view.searchBody
        searchEngineUnit = view.searchEngineUnit
        searchEngineFrom = view.searchEngineFrom
        searchEngineTo = view.searchEngineTo
        searchColor = view.searchColor
        searchPriceFrom = view.searchPriceFrom
        searchPriceTo = view.searchPriceTo
        searchGearboxMech = view.searchGearboxMech
        searchGearboxAutom = view.searchGearboxAutom
        searchPetrol = view.searchPetrol
        searchDiesel = view.searchDiesel
        searchGasPetrol = view.searchGasPetrol
        searchElectro = view.searchElectro
        searchGas = view.searchGas
        searchPetrolElectro = view.searchPetrolElectro

        val applyButton: Button? = view.applyButton
        val resetButton: Button? = view.resetButton

        val bundle = arguments
        searchData = bundle?.getSerializable("searchData") as SearchData
        setDataToSpinner(searchData)

        searchMark?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                val item = parent.getItemAtPosition(position)
                val key = searchData?.mark?.let { getKeyFromValue(it, item).toString() }
                if ("0" != key) {
                    getModel(key)
                } else {
                    fulfillSpinner(ArrayList(), searchModel)
                }
            }


            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        searchRegion?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                val item = parent.getItemAtPosition(position)
                val key = searchData?.region?.let { getKeyFromValue(it, item).toString() }
                if ("0" != key) {
                    getCity(key)
                } else {
                    fulfillSpinner(ArrayList(), searchCity)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        resetButton?.setOnClickListener { resetViewValues() }

        applyButton?.setOnClickListener {

            queryDetails = QueryDetails()
            getDataFromSpinners()
            getDataFromCheckbox()
            reviewIsChecked()

            val bundle1 = Bundle()
            bundle1.putSerializable("queryDetails", queryDetails)
            val carsListFragment = CarsListFragment()
            carsListFragment.arguments = bundle1
            fragmentManager?.let { FragmentUtil.replaceFragment(it, carsListFragment, true) }


        }

        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }


    private fun setDataToSpinner(searchData: SearchData?) {
        fulfillSpinner(ArrayList(searchData?.mark!!.values), searchMark)
        fulfillSpinner(ArrayList(searchData.region!!.values), searchRegion)
        fulfillSpinner(ArrayList(searchData.year!!.values), searchYearFrom)
        fulfillSpinner(ArrayList(searchData.year!!.values), searchYearTo)
        fulfillSpinner(ArrayList(searchData.body!!.values), searchBody)
        fulfillSpinner(ArrayList(searchData.drive!!.values), searchEngineUnit)
        fulfillSpinner(ArrayList(searchData.engine!!.values), searchEngineFrom)
        fulfillSpinner(ArrayList(searchData.engine!!.values), searchEngineTo)
        fulfillSpinner(ArrayList(searchData.color!!.values), searchColor)
    }

    fun fulfillSpinner(arrayList: ArrayList<String>, spinner: Spinner?) {
        Collections.sort(arrayList, String.CASE_INSENSITIVE_ORDER)
        val adapter =
            ArrayAdapter(Objects.requireNonNull<Context>(context), android.R.layout.simple_spinner_item, arrayList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = adapter
    }

    private fun getDataFromSpinners() {
        queryDetails?.body = (
                if (searchData != null) getKeyFromValue(
                    searchData?.body,
                    if (searchBody != null) searchBody?.selectedItem.toString() else ""
                ).toString() else ""
                )
        queryDetails?.color = (
                if (searchData != null) getKeyFromValue(
                    searchData?.color,
                    if (searchColor != null) searchColor?.selectedItem.toString() else ""
                ).toString() else ""
                )
        queryDetails?.engineUnit = (
                if (searchData != null) getKeyFromValue(
                    searchData?.drive,
                    if (searchEngineUnit != null) searchEngineUnit?.selectedItem.toString() else ""
                ).toString() else ""
                )
        queryDetails?.mark = (
                if (searchData != null) getKeyFromValue(
                    searchData?.mark,
                    if (searchMark != null) searchMark?.selectedItem.toString() else ""
                ).toString() else ""
                )

        queryDetails?.model = (
                if (model != null) getKeyFromValue(
                    model?.model,
                    if (searchModel != null) searchModel!!.selectedItem.toString() else ""
                ).toString() else ""
                )
        queryDetails?.region = (
                if (searchData != null) getKeyFromValue(
                    searchData?.region,
                    if (searchRegion != null) searchRegion?.selectedItem.toString() else ""
                ).toString() else ""
                )
        queryDetails?.city = (
                if (city != null) getKeyFromValue(
                    city?.city,
                    if (searchCity != null) searchCity!!.selectedItem.toString() else ""
                ).toString() else ""
                )
        queryDetails?.engineFrom = (
                if (city != null) getKeyFromValue(
                    searchData?.engine,
                    if (searchEngineFrom != null) searchEngineFrom!!.selectedItem.toString() else ""
                ).toString() else ""
                )
        queryDetails?.engineTo = (
                if (city != null) getKeyFromValue(
                    searchData?.engine,
                    if (searchEngineTo != null) searchEngineTo!!.selectedItem.toString() else ""
                ).toString() else ""
                )
        queryDetails?.yearFrom = (
                if (city != null) getKeyFromValue(
                    searchData?.year,
                    if (searchYearFrom != null) searchYearFrom!!.selectedItem.toString() else ""
                ).toString() else ""
                )
        queryDetails?.yearTo = (
                if (city != null) getKeyFromValue(
                    searchData?.year,
                    if (searchYearTo != null) searchYearTo!!.selectedItem.toString() else ""
                ).toString() else ""
                )

    }

    private fun getDataFromCheckbox() {
        queryDetails?.priceFrom = (searchPriceFrom!!.text.toString())
        queryDetails?.priceTo = (searchPriceTo!!.text.toString())
    }

    private fun reviewIsChecked() {

        if (searchGearboxMech!!.isChecked) {
            queryDetails!!.gearboxMech = "1"
        } else
            queryDetails!!.gearboxMech = ""

        if (searchGearboxAutom!!.isChecked) {
            queryDetails!!.gearboxAutom = "2"
        } else
            queryDetails!!.gearboxAutom = ""

        if (searchPetrol!!.isChecked) {
            queryDetails!!.petrol = "1"
        } else
            queryDetails!!.petrol = ""

        if (searchDiesel!!.isChecked) {
            queryDetails!!.diesel = "2"
        } else
            queryDetails!!.diesel = ""

        if (searchGasPetrol!!.isChecked) {
            queryDetails!!.gasPetrol = "4"
        } else
            queryDetails!!.gasPetrol = ""

        if (searchElectro!!.isChecked) {
            queryDetails!!.electro = "6"
        } else
            queryDetails!!.electro = ""

        if (searchGas!!.isChecked) {
            queryDetails!!.gas = "3"
        } else
            queryDetails!!.gas = ""

        if (searchPetrolElectro!!.isChecked) {
            queryDetails!!.petrolElectro = "5"
        } else
            queryDetails!!.petrolElectro = ""
    }

    private fun resetViewValues() {
        setDataToSpinner(searchData)

        searchPriceFrom!!.setText("")
        searchPriceTo!!.setText("")

        if (searchGearboxMech!!.isChecked) {
            searchGearboxMech!!.isChecked = false
        }

        if (searchGearboxAutom!!.isChecked) {
            searchGearboxAutom!!.isChecked = false
        }

        if (searchPetrol!!.isChecked) {
            searchPetrol!!.isChecked = false
        }

        if (searchDiesel!!.isChecked) {
            searchDiesel!!.isChecked = false
        }

        if (searchGasPetrol!!.isChecked) {
            searchGasPetrol!!.isChecked = false
        }

        if (searchElectro!!.isChecked) {
            searchElectro!!.isChecked = false
        }

        if (searchGas!!.isChecked) {
            searchGas!!.isChecked = false
        }

        if (searchPetrolElectro!!.isChecked) {
            searchPetrolElectro!!.isChecked = false
        }

    }

    @SuppressLint("CheckResult")
    private fun getModel(mark: String?) {
        Api
            .service
            .getModel(mark)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseBody ->
                val htmlParser = HTMLParser()
                val html = responseBody.string()
                model = htmlParser.getModel(html)
                fulfillSpinner(ArrayList(model!!.model!!.values), searchModel)

            }, { throwable -> throwable.printStackTrace() })
    }

    @SuppressLint("CheckResult")
    private fun getCity(region: String?) {
        Api
            .service
            .getCity(region)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseBody ->
                val htmlParser = HTMLParser()
                val html = responseBody.string()
                city = htmlParser.getCity(html)
                fulfillSpinner(ArrayList(city!!.city!!.values), searchCity)

            }, { throwable -> throwable.printStackTrace() })
    }

    companion object {

        fun getKeyFromValue(map: Map<String, String>?, value: Any): Any? {
            for (`object` in map?.keys!!) {
                if (Objects.requireNonNull<Any>(map[`object`]) == value) {
                    return `object`
                }
            }
            return null
        }
    }
}


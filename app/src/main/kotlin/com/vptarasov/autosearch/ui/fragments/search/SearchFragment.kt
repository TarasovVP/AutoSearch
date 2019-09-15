package com.vptarasov.autosearch.ui.fragments.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.di.component.DaggerFragmentComponent
import com.vptarasov.autosearch.di.module.FragmentModule
import com.vptarasov.autosearch.model.City
import com.vptarasov.autosearch.model.Model
import com.vptarasov.autosearch.model.QueryDetails
import com.vptarasov.autosearch.model.SearchData
import com.vptarasov.autosearch.ui.fragments.cars_list.CarsListFragment
import com.vptarasov.autosearch.util.FragmentUtil
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.fragment_search_buttons.view.*
import kotlinx.android.synthetic.main.fragment_search_checkboxes.view.*
import java.util.*
import javax.inject.Inject

class SearchFragment : Fragment(), SearchContract.View {

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

    @Inject
    lateinit var presenter: SearchContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        injectDependency()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        initView(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bundle = arguments
        searchData = bundle?.getSerializable("searchData") as SearchData
        setDataToSpinner(searchData)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unsubscribe()
    }

    private fun injectDependency() {
        val fragmentComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        fragmentComponent.inject(this)
    }

    override fun initView(view: View) {
        searchModel = view.searchModel
        searchCity = view.searchCity
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

        searchMark = view.searchMark
        searchMark?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position)
                val key = searchData?.mark?.let { getKeyFromValue(
                    it,
                    item
                ).toString() }
                if ("0" != key) {
                    presenter.getModel(key, searchModel)
                } else {
                    fulfillSpinner(ArrayList(), searchModel)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        searchRegion = view.searchRegion
        searchRegion?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position)
                val key = searchData?.region?.let { getKeyFromValue(
                    it,
                    item
                ).toString() }
                if ("0" != key) {
                    presenter.getCity(key, searchCity)
                } else {
                    fulfillSpinner(ArrayList(), searchCity)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val resetButton: Button? = view.resetButton
        resetButton?.setOnClickListener { resetViewValues() }

        val applyButton: Button? = view.applyButton
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
    }

    override fun setDataToSpinner(searchData: SearchData?) {
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

    override fun fulfillSpinner(arrayList: ArrayList<String>, spinner: Spinner?) {
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
                    if (searchModel != null) searchModel?.selectedItem.toString() else ""
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
                    if (searchCity != null) searchCity?.selectedItem.toString() else ""
                ).toString() else ""
                )
        queryDetails?.engineFrom = (
                if (city != null) getKeyFromValue(
                    searchData?.engine,
                    if (searchEngineFrom != null) searchEngineFrom?.selectedItem.toString() else ""
                ).toString() else ""
                )
        queryDetails?.engineTo = (
                if (city != null) getKeyFromValue(
                    searchData?.engine,
                    if (searchEngineTo != null) searchEngineTo?.selectedItem.toString() else ""
                ).toString() else ""
                )
        queryDetails?.yearFrom = (
                if (city != null) getKeyFromValue(
                    searchData?.year,
                    if (searchYearFrom != null) searchYearFrom?.selectedItem.toString() else ""
                ).toString() else ""
                )
        queryDetails?.yearTo = (
                if (city != null) getKeyFromValue(
                    searchData?.year,
                    if (searchYearTo != null) searchYearTo?.selectedItem.toString() else ""
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

    override fun resetViewValues() {
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


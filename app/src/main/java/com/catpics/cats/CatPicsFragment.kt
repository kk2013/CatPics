package com.catpics.cats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.catpics.CatPicsApplication
import com.catpics.R
import com.catpics.di.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.cat_pics_fragment.*
import javax.inject.Inject
import kotlin.math.roundToInt

class CatPicsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val catPicsAdapter = CatsPicsAdapter()
    private lateinit var gridLayoutManager: GridLayoutManager

    private val catPicsViewModel: CatPicsViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
                             ): View? {

        val view = inflater.inflate(R.layout.cat_pics_fragment, container, false)

        /*cats_recycler_view.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    cats_recycler_view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val width = resources.displayMetrics.widthPixels
                    val imageWidth = resources.getDimension(R.dimen.image_width)
                    val spanCount = (width / imageWidth).roundToInt()
                    gridLayoutManager.spanCount = spanCount
                    gridLayoutManager.requestLayout()
                }
            }
        )

        gridLayoutManager = GridLayoutManager(activity, 2)
        cats_recycler_view.layoutManager = gridLayoutManager
        cats_recycler_view.adapter = catPicsAdapter*/

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cats_recycler_view.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    cats_recycler_view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val width = resources.displayMetrics.widthPixels
                    val imageWidth = resources.getDimension(R.dimen.image_width)
                    val spanCount = (width / imageWidth).roundToInt()
                    gridLayoutManager.spanCount = spanCount
                    gridLayoutManager.requestLayout()
                }
            }
        )

        gridLayoutManager = GridLayoutManager(activity, 2)
        cats_recycler_view.layoutManager = gridLayoutManager
        cats_recycler_view.adapter = catPicsAdapter

        catPicsViewModel.getCatPics().observe(this, Observer {
            catPicsAdapter.submitList(it)
        })

        catPicsViewModel.getCatPics()
    }
}
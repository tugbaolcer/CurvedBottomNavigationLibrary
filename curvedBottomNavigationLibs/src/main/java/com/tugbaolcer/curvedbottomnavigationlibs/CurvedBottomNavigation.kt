package com.tugbaolcer.curvedbottomnavigationlibs

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tugbaolcer.curvedbottomnavigationlibs.databinding.LayoutFabIconBinding


class CurvedBottomNavigation : BottomNavigationView {

    private var binding = LayoutFabIconBinding.inflate(LayoutInflater.from(context), this, true)


    private var path: Path? = null
    private var paint: Paint? = null

    /**fab button*/
    val curveCircleRadius = 90

    var firstCurveStartPoint = Point()
    var firstCurveEndPoint = Point()
    var firstCurveControlPointX = Point()
    var firstCurveControlPointY = Point()

    var secondCurveStartPoint = Point()
    var secondCurveEndPoint = Point()
    var secondCurveControlPointX = Point()
    var secondCurveControlPointY = Point()

    var bottomNavBarWidth: Int = 0
    var bottomNavBarHeight: Int = 0

    var customBackgroundColor: Int = 0
    var customFabIconColor: Int = 0
    var customFabIconBackground: Int = 0
    var customFabCircleColor: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs:AttributeSet): super(context, attrs){
        context.theme.obtainStyledAttributes(attrs, R.styleable.CurvedBottomNavigation, 0, 0).let { typedArray ->
            customBackgroundColor = typedArray.getColor(R.styleable.CurvedBottomNavigation_cbn_background, ContextCompat.getColor(context, R.color.white))
            customFabIconColor = typedArray.getColor(R.styleable.CurvedBottomNavigation_cbn_fab_icon_color, ContextCompat.getColor(context, R.color.white))
            customFabIconBackground = typedArray.getColor(R.styleable.CurvedBottomNavigation_cbn_fab_icon_background, ContextCompat.getColor(context, R.color.white))
            customFabCircleColor = typedArray.getColor(R.styleable.CurvedBottomNavigation_cbn_fab_circle_color, ContextCompat.getColor(context, R.color.white))
        }
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun init() {
        path = Path()
        paint = Paint()

        //FILL sadece daire doldururken FILL_AND_STROKE daire cizgilerini belirler.
        paint!!.style = Paint.Style.FILL_AND_STROKE
        paint!!.color = customBackgroundColor
        setBackgroundColor(Color.TRANSPARENT)
        itemBackgroundResource = R.drawable.bg_inset
        post { selectedItemId = R.id.nav_categories }
        binding.apply {
            linId.backgroundTintList = ColorStateList.valueOf(customFabCircleColor)
            linId.children.forEach { view ->
                (view as FloatingActionButton).apply {
                    imageTintList = ColorStateList.valueOf(customFabIconColor)
                    backgroundTintList = ColorStateList.valueOf(customFabIconBackground)
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        bottomNavBarHeight = height
        bottomNavBarWidth = width


        firstCurveStartPoint.set(bottomNavBarWidth / 2 - curveCircleRadius * 2 - curveCircleRadius / 3, 0)

        firstCurveEndPoint.set(bottomNavBarWidth / 2, curveCircleRadius + curveCircleRadius / 4)

        secondCurveStartPoint = firstCurveEndPoint

        secondCurveEndPoint.set(bottomNavBarWidth / 2 + curveCircleRadius * 2 + curveCircleRadius / 3, 0)

        //first
        firstCurveControlPointX.set(firstCurveStartPoint.x + curveCircleRadius + curveCircleRadius / 4, firstCurveStartPoint.y)

        firstCurveControlPointY.set(firstCurveEndPoint.x - curveCircleRadius * 2 + curveCircleRadius, firstCurveEndPoint.y)

        //second
        secondCurveControlPointX.set(secondCurveStartPoint.x + curveCircleRadius * 2 - curveCircleRadius, secondCurveStartPoint.y)

        secondCurveControlPointY.set(secondCurveEndPoint.x - (curveCircleRadius + curveCircleRadius / 4), secondCurveEndPoint.y)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        path!!.apply {
            reset()
            moveTo(0f, 0f)
            lineTo(firstCurveStartPoint.x.toFloat(), firstCurveStartPoint.y.toFloat())

            cubicTo(
                firstCurveControlPointX.x.toFloat(), firstCurveControlPointX.y.toFloat(),
                firstCurveControlPointY.x.toFloat(), firstCurveControlPointY.y.toFloat(),
                firstCurveEndPoint.x.toFloat(), firstCurveEndPoint.y.toFloat()
            )

            cubicTo(
                secondCurveControlPointX.x.toFloat(), secondCurveControlPointX.y.toFloat(),
                secondCurveControlPointY.x.toFloat(), secondCurveControlPointY.y.toFloat(),
                secondCurveEndPoint.x.toFloat(), secondCurveEndPoint.y.toFloat()
            )

            lineTo(bottomNavBarWidth.toFloat(), 0f)
            lineTo(bottomNavBarWidth.toFloat(), bottomNavBarHeight.toFloat())
            lineTo(0f, bottomNavBarHeight.toFloat())
            close()
        }
        canvas.translate(0f, 110f)
        canvas.drawPath(path!!, paint!!)
    }

     fun draw(i: Int, bottomNavigation: CurvedBottomNavigation) {
        bottomNavigation.let { navigation ->
            firstCurveStartPoint.set(
                navigation.bottomNavBarWidth / i - navigation.curveCircleRadius * 2 -
                        navigation.curveCircleRadius / 3, 0
            )
            firstCurveEndPoint.set(
                navigation.bottomNavBarWidth / i,
                navigation.curveCircleRadius + navigation.curveCircleRadius / 4
            )

            secondCurveStartPoint = navigation.firstCurveEndPoint
            secondCurveEndPoint.set(
                navigation.bottomNavBarWidth / i + navigation.curveCircleRadius * 2 + navigation.curveCircleRadius / 3,
                0
            )

            firstCurveControlPointX.set(
                navigation.firstCurveStartPoint.x + navigation.curveCircleRadius + navigation.curveCircleRadius / 4,
                navigation.firstCurveStartPoint.y
            )

            firstCurveControlPointY.set(
                navigation.firstCurveEndPoint.x - navigation.curveCircleRadius * 2 + navigation.curveCircleRadius,
                navigation.firstCurveEndPoint.y
            )

            secondCurveControlPointX.set(
                navigation.secondCurveStartPoint.x + navigation.curveCircleRadius * 2 - navigation.curveCircleRadius,
                navigation.secondCurveStartPoint.y
            )

            secondCurveControlPointY.set(
                navigation.secondCurveEndPoint.x - (navigation.curveCircleRadius + navigation.curveCircleRadius / 4),
                navigation.secondCurveEndPoint.y
            )
        }


    }

     fun draw(bottomNavigation: CurvedBottomNavigation) {

        bottomNavigation.let {navigation->
            firstCurveStartPoint.set(
                navigation.bottomNavBarWidth * 10 / 12 - navigation.curveCircleRadius * 2 -
                        navigation.curveCircleRadius / 3, 0
            )
            firstCurveEndPoint.set(
                navigation.bottomNavBarWidth * 10 / 12,
                navigation.curveCircleRadius + navigation.curveCircleRadius / 4
            )

            secondCurveStartPoint = navigation.firstCurveEndPoint
            secondCurveEndPoint.set(
                navigation.bottomNavBarWidth * 10 / 12 + navigation.curveCircleRadius * 2 + navigation.curveCircleRadius / 3,
                0
            )

            firstCurveControlPointX.set(
                navigation.firstCurveStartPoint.x + navigation.curveCircleRadius + navigation.curveCircleRadius / 4,
                navigation.firstCurveStartPoint.y
            )

            firstCurveControlPointY.set(
                navigation.firstCurveEndPoint.x - navigation.curveCircleRadius * 2 + navigation.curveCircleRadius,
                navigation.firstCurveEndPoint.y
            )

            secondCurveControlPointX.set(
                navigation.secondCurveStartPoint.x + navigation.curveCircleRadius * 2 - navigation.curveCircleRadius,
                navigation.secondCurveStartPoint.y
            )

            secondCurveControlPointY.set(
                navigation.secondCurveEndPoint.x - (navigation.curveCircleRadius + navigation.curveCircleRadius / 4),
                navigation.secondCurveEndPoint.y
            )
        }

    }


    fun changedFabIcon(bottomNavigation: CurvedBottomNavigation, menuItem: MenuItem ){
        binding.apply {
            when(menuItem.itemId){
                R.id.nav_home-> {
                    bottomNavigation.draw(6, bottomNavigation)
                    linId.x = bottomNavigation.firstCurveControlPointX.x.toFloat()
                    fab1.visibility = View.VISIBLE
                    fab2.visibility = View.GONE
                    fab3.visibility = View.GONE
                    menu.findItem(R.id.nav_home).icon = null
                    menu.findItem(R.id.nav_categories).icon = ContextCompat.getDrawable(context, R.drawable.ic_white_category)
                    menu.findItem(R.id.nav_profile).icon = ContextCompat.getDrawable(context, R.drawable.ic_white_account)
                }
                R.id.nav_categories-> {
                    bottomNavigation.draw(2, bottomNavigation)
                    linId.x = bottomNavigation.firstCurveControlPointX.x.toFloat()
                    fab1.visibility = View.GONE
                    fab2.visibility = View.VISIBLE
                    fab3.visibility = View.GONE
                    menu.findItem(R.id.nav_home).icon = ContextCompat.getDrawable(context, R.drawable.ic_white_home)
                    menu.findItem(R.id.nav_categories).icon = null
                    menu.findItem(R.id.nav_profile).icon = ContextCompat.getDrawable(context, R.drawable.ic_white_account)
                }
                R.id.nav_profile-> {
                    bottomNavigation.draw( bottomNavigation)
                    linId.x = bottomNavigation.firstCurveControlPointX.x.toFloat()
                    fab1.visibility = View.GONE
                    fab2.visibility = View.GONE
                    fab3.visibility = View.VISIBLE
                    menu.findItem(R.id.nav_home).icon = ContextCompat.getDrawable(context, R.drawable.ic_white_home)
                    menu.findItem(R.id.nav_categories).icon = ContextCompat.getDrawable(context, R.drawable.ic_white_category)
                    menu.findItem(R.id.nav_profile).icon = null

                }
            }
        }

    }

}
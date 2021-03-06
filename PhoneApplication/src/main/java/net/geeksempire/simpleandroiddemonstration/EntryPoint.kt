package net.geeksempire.simpleandroiddemonstration

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.abanabsalan.aban.magazine.HomePageConfigurations.UI.Adapters.InstagramStoryHighlights.AllUsersAdapter
import com.abanabsalan.aban.magazine.HomePageConfigurations.UI.Adapters.InstagramStoryHighlights.PassUserDataProcess
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import net.geeksempire.simpleandroiddemonstration.DatabaseProcess.AfterBackgroundProcess
import net.geeksempire.simpleandroiddemonstration.DatabaseProcess.CoroutinesProcess
import net.geeksempire.simpleandroiddemonstration.Extensions.setupColorsOfViews
import net.geeksempire.simpleandroiddemonstration.databinding.EntryPointViewBinding
import net.geeksempire.simpleandroiddemonstration.databinding.IconsShapesPreferencesBinding
import net.geekstools.floatshort.PRO.Widgets.RoomDatabase.DatabaseInterface
import net.geekstools.supershortcuts.PRO.Utils.UI.Gesture.GestureConstants
import net.geekstools.supershortcuts.PRO.Utils.UI.Gesture.GestureListenerConstants
import net.geekstools.supershortcuts.PRO.Utils.UI.Gesture.GestureListenerInterface
import net.geekstools.supershortcuts.PRO.Utils.UI.Gesture.SwipeGestureListener

class EntryPoint : AppCompatActivity(), GestureListenerInterface, PassUserDataProcess, AfterBackgroundProcess {

    lateinit var allUsersAdapter: AllUsersAdapter

    lateinit var entryPointViewBinding: EntryPointViewBinding

    var databaseSize = 0

    val hashMap: HashMap<String, String> = HashMap<String, String>()

    //Gesture Configuration
    private val swipeGestureListener: SwipeGestureListener by lazy {
        SwipeGestureListener(applicationContext,this@EntryPoint)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entryPointViewBinding = EntryPointViewBinding.inflate(layoutInflater)
        setContentView(entryPointViewBinding.root)

        /*HashMap Samples*/
        hashMap.put("A-Text-Key"/*Key*/, "Test"/*Value*/)

        hashMap.get("A-Text-Key")

        hashMap.entries.forEach {

            it.key

            it.value

        }

        hashMap.replace("A-Key", "A New Value")

        hashMap.entries.sortedByDescending {

            it.value
        }

        setupColorsOfViews()

        allUsersAdapter = AllUsersAdapter(applicationContext, this@EntryPoint)

        entryPointViewBinding.recyclerView.layoutManager = GridLayoutManager(applicationContext, 2, RecyclerView.VERTICAL, false)

        entryPointViewBinding.recyclerView.adapter = allUsersAdapter

        entryPointViewBinding.addNewUser.setOnClickListener {

            CoroutineScope(Dispatchers.Main).async {

                CoroutinesProcess()
                    .testFlowOfData().collect {

                        println(">>> Collected Data = " + it)


                    }

            }

            //This is REST API
            //https://abanabsalan.com/wp-json/wp/v2/posts/4749

//            HttpsRequests(applicationContext)
//                .getJsonDataFromServer()

            //For Example Id Of Product -> 4749
//            val productId = 4749
//            CoroutineScope(Dispatchers.IO).launch {
//
//                val rawJsonData = URL("https://abanabsalan.com/wp-json/wp/v2/posts/${productId}")
//                    .readText(Charset.defaultCharset())
//                println(">>> " + rawJsonData)
//
//                val jsonParser = JsonParser()
//
//                jsonParser.prepareJsonData(rawJsonData)
//
//            }


            //startActivity(Intent(applicationContext, PlayWithServicesActivity::class.java))

            /*startActivity(Intent(applicationContext, WorkManagerActivity::class.java),
                ActivityOptions.makeCustomAnimation(applicationContext, R.anim.slide_from_right, 0).toBundle())*/

            /*Handler(Looper.getMainLooper()).postDelayed({

                startActivity(Intent(applicationContext, AddNewUser::class.java),
                        ActivityOptions.makeCustomAnimation(applicationContext, R.anim.slide_from_right, 0).toBundle())

            }, 500)*/

            /* Database Example */
            /*val databaseModel: DatabaseModel = DatabaseModel(
                    uniqueUsername = "666",
                    emailAddress = "666@gmail.com",
                    phoneNumber = "00666"
            )

            val widgetDataInterface = Room.databaseBuilder(applicationContext, DatabaseInterface::class.java, DatabaseName)
                    .build()

            CoroutineScope(Dispatchers.IO).launch {

                val dao = widgetDataInterface.initializeDataAccessObject()

                dao.insertNewWidgetDataSuspend(databaseModel)


                dao.getAllWidgetDataSuspend().forEach { databaseModel ->

                    println(">>>>>>>>>>>>>>>>>> ${databaseModel}")

                }

            }*/
            /* Database Example */

        }

        (application as SimpleApplication).dependencyInjectionBuilder.userInformationProcess.setupAdapterData(this@EntryPoint, this@EntryPoint)

        databaseSize = (application as SimpleApplication).dependencyInjectionBuilder.userInformationProcess.databaseSize(applicationContext)

        entryPointViewBinding.searchView.setOnEditorActionListener { view, actionId, keyEvent ->

            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {

                    val searchResult = (application as SimpleApplication).dependencyInjectionBuilder.userInformationProcess.searchInDatabase(applicationContext, view.text.toString())

                    if (searchResult.isEmpty()) {



                    } else {
                        Log.d("Search Result", "${searchResult}")


                    }

                }
            }

            false
        }

        entryPointViewBinding.searchAction.setOnClickListener {

            val fadeAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.down_up)
            entryPointViewBinding.searchAction.startAnimation(fadeAnimation)

            val searchResult = (application as SimpleApplication).dependencyInjectionBuilder.userInformationProcess
                    .searchInDatabase(applicationContext, entryPointViewBinding.searchView.text.toString())

            if (searchResult.isEmpty()) {


            } else {
                Log.d("Search Result", "${searchResult}")

            }

        }

        entryPointViewBinding.searchAction.setColorFilter(R.color.black,
            android.graphics.PorterDuff.Mode.MULTIPLY)
        entryPointViewBinding.searchAction.clearColorFilter()

        entryPointViewBinding.searchView.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {



            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                //Interrupt the Thread

            }

            override fun afterTextChanged(s: Editable?) {

                //Call Search Functions and Then Start the Thread

            }

        })


        //Background Process
        val coroutinesProcess = CoroutinesProcess()


        //Using Coroutine Launch
        coroutinesProcess.testFunctionsLaunch()


        //Using Coroutine Asyc
        coroutinesProcess.testFunctionsAsync()

        //Using Coroutine Asyc
        CoroutineScope(Dispatchers.IO).launch {

            //

            val result = coroutinesProcess.testFunctionsAsyncReturnResult().await()

            //Use Result On UI
            withContext(Dispatchers.Main) {

                //Use Result Here For UI

            }

        }

    }

    override fun onStart() {
        super.onStart()

        //Call Migration Once.
        CoroutineScope(Dispatchers.IO).launch {

            val databaseMigration = object : Migration(1, 2) {

                override fun migrate(sqLiteDatabase: SupportSQLiteDatabase) {

                    sqLiteDatabase
                            .execSQL("ALTER TABLE WidgetData ADD COLUMN sex INTEGER NOT NULL DEFAULT 0")

                }

            }

            val roomDatabase = Room.databaseBuilder(applicationContext, DatabaseInterface::class.java, "WidgetData")

            roomDatabase.addMigrations(databaseMigration)

        }

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()

        if ((application as SimpleApplication).dependencyInjectionBuilder.userInformationProcess.databaseSize(applicationContext) > databaseSize) {

            (application as SimpleApplication).dependencyInjectionBuilder.userInformationProcess.setupAdapterData(this@EntryPoint, this@EntryPoint)

        }


        entryPointViewBinding.addNewUser.setOnTouchListener { view, motionEvent ->
            println(">>> Touch")

            val initialX = motionEvent.x
            val initialY = motionEvent.y

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    println(">>> Down")


                }
                MotionEvent.ACTION_UP -> {
                    println(">>> Up")

                }
                MotionEvent.ACTION_MOVE -> {
                    println(">>> Move")

                    entryPointViewBinding.addNewUser.y = motionEvent.y
                    entryPointViewBinding.addNewUser.x = motionEvent.x

                }
            }

            false
        }

    }

    override fun onBackPressed() {

        entryPointViewBinding.deleteView.visibility = View.INVISIBLE

    }

    //Gesture Listener
    override fun onSwipeGesture(gestureConstants: GestureConstants, downMotionEvent: MotionEvent, moveMotionEvent: MotionEvent, initVelocityX: Float, initVelocityY: Float) {
        super.onSwipeGesture(gestureConstants, downMotionEvent, moveMotionEvent, initVelocityX, initVelocityY)

        when (gestureConstants) {
            is GestureConstants.SwipeVertical -> {
                when (gestureConstants.verticallDirection) {
                    GestureListenerConstants.SWIPE_DOWN -> {
                        println("*** Swipe Down ***")

                        entryPointViewBinding.searchView.requestFocus()

                        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.showSoftInput(entryPointViewBinding.searchView, InputMethodManager.SHOW_IMPLICIT)

                    }
                    GestureListenerConstants.SWIPE_UP -> {
                        println("*** Swipe Up ***")


                    }
                }
            }
            is GestureConstants.SwipeHorizontal -> {
                when (gestureConstants.horizontalDirection) {
                    GestureListenerConstants.SWIPE_LEFT -> {
                        println("*** Swipe Left ***")


                    }
                    GestureListenerConstants.SWIPE_RIGHT -> {
                        println("*** Swipe Right ***")


                    }
                }
            }
        }

    }

    override fun dispatchTouchEvent(motionEvent: MotionEvent?): Boolean {
        motionEvent?.let {
            swipeGestureListener.onTouchEvent(it)
        }

        return super.dispatchTouchEvent(motionEvent)
    }

    override fun userDataToDelete(specificDataKey: String, specificDataPosition: Int) {

        println("*** 2. ${specificDataKey} -- ${specificDataPosition}")

        entryPointViewBinding.deleteView.visibility = View.VISIBLE

        entryPointViewBinding.deleteView.setOnClickListener {

            allUsersAdapter.allUsersData.removeAt(specificDataPosition)

            (application as SimpleApplication).dependencyInjectionBuilder.userInformationProcess.deleteSpecificData(applicationContext, specificDataKey)

            allUsersAdapter.notifyItemRemoved(specificDataPosition)
            allUsersAdapter.notifyItemRangeChanged(specificDataPosition, allUsersAdapter.allUsersData.size)

            databaseSize = (application as SimpleApplication).dependencyInjectionBuilder.userInformationProcess.databaseSize(applicationContext)

            entryPointViewBinding.deleteView.visibility = View.INVISIBLE

        }

    }

    override fun notifyUserInterfaceForData() {

        runOnUiThread {

            //Foreground
            allUsersAdapter.notifyDataSetChanged()

        }

    }

    fun openCustomDialogue(activity: AppCompatActivity) {

        val dialogueWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                377f,
                activity.resources.displayMetrics).toInt()

        val dialogueHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                387f,
                activity.resources.displayMetrics).toInt()

        //Custom Dialogue Configurations
        val layoutParams = WindowManager.LayoutParams()

        layoutParams.width = dialogueWidth
        layoutParams.height = dialogueHeight
        layoutParams.windowAnimations = android.R.style.Animation_Dialog
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.57f

        val iconsShapesPreferencesBinding = IconsShapesPreferencesBinding.inflate(layoutInflater)

        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(iconsShapesPreferencesBinding.root)
        dialog.window!!.attributes = layoutParams
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.decorView.setBackgroundColor(Color.TRANSPARENT)
        dialog.setCancelable(true)
        //Custom Dialogue Configurations

        iconsShapesPreferencesBinding.dialogueTitle.text = "Sun Software"

        iconsShapesPreferencesBinding.dialogueTitle.setOnClickListener {

            Toast.makeText(applicationContext, "Sun Software 👍", Toast.LENGTH_LONG).show()

            dialog.dismiss()
        }


        dialog.setOnDismissListener {

            dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        dialog.show()
    }

}
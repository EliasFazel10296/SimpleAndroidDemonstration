package net.geeksempire.simpleandroiddemonstration

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abanabsalan.aban.magazine.HomePageConfigurations.UI.Adapters.InstagramStoryHighlights.AllUsersAdapter
import com.abanabsalan.aban.magazine.HomePageConfigurations.UI.Adapters.InstagramStoryHighlights.PassUserDataProcess
import net.geeksempire.simpleandroiddemonstration.DatabaseProcess.AfterBackgroundProcess
import net.geeksempire.simpleandroiddemonstration.DatabaseProcess.UserInformationProcess
import net.geeksempire.simpleandroiddemonstration.Extensions.setupColorsOfViews
import net.geeksempire.simpleandroiddemonstration.SaveProcess.AddNewUser
import net.geeksempire.simpleandroiddemonstration.databinding.EntryPointViewBinding
import net.geekstools.supershortcuts.PRO.Utils.UI.Gesture.GestureConstants
import net.geekstools.supershortcuts.PRO.Utils.UI.Gesture.GestureListenerConstants
import net.geekstools.supershortcuts.PRO.Utils.UI.Gesture.GestureListenerInterface
import net.geekstools.supershortcuts.PRO.Utils.UI.Gesture.SwipeGestureListener

class EntryPoint : AppCompatActivity(), GestureListenerInterface, PassUserDataProcess, AfterBackgroundProcess {

    val userInformationProcess: UserInformationProcess = UserInformationProcess()

    lateinit var allUsersAdapter: AllUsersAdapter

    lateinit var entryPointViewBinding: EntryPointViewBinding

    var databaseSize = 0

    val hashMap: HashMap<String, String> = HashMap<String, String>()

    private val swipeGestureListener: SwipeGestureListener by lazy {
        SwipeGestureListener(applicationContext, this@EntryPoint)
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

            startActivity(Intent(applicationContext, AddNewUser::class.java))

        }

        userInformationProcess.setupAdapterData(this@EntryPoint, this@EntryPoint)

        databaseSize = userInformationProcess.databaseSize(applicationContext)

        entryPointViewBinding.searchView.setOnEditorActionListener { view, actionId, keyEvent ->

            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {

                    val searchResult = userInformationProcess.searchInDatabase(applicationContext, view.text.toString())

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

            val searchResult = userInformationProcess
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

    }

    override fun onResume() {
        super.onResume()

        if (userInformationProcess.databaseSize(applicationContext) > databaseSize) {

            userInformationProcess.setupAdapterData(this@EntryPoint, this@EntryPoint)

        }

    }

    override fun onBackPressed() {

        entryPointViewBinding.deleteView.visibility = View.INVISIBLE

    }

    override fun onSwipeGesture(gestureConstants: GestureConstants, downMotionEvent: MotionEvent, moveMotionEvent: MotionEvent, initVelocityX: Float, initVelocityY: Float) {
        super.onSwipeGesture(gestureConstants, downMotionEvent, moveMotionEvent, initVelocityX, initVelocityY)

        when (gestureConstants) {
            is GestureConstants.SwipeVertical -> {
                when (gestureConstants.verticallDirection) {
                    GestureListenerConstants.SWIPE_DOWN -> {



                    }
                    GestureListenerConstants.SWIPE_UP -> {



                    }
                }
            }
            is GestureConstants.SwipeHorizontal -> {
                when (gestureConstants.horizontalDirection) {
                    GestureListenerConstants.SWIPE_LEFT -> {



                    }
                    GestureListenerConstants.SWIPE_RIGHT -> {



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

            userInformationProcess.deleteSpecificData(applicationContext, specificDataKey)

            allUsersAdapter.notifyItemRemoved(specificDataPosition)
            allUsersAdapter.notifyItemRangeChanged(specificDataPosition, allUsersAdapter.allUsersData.size)

            databaseSize = userInformationProcess.databaseSize(applicationContext)

            entryPointViewBinding.deleteView.visibility = View.INVISIBLE

        }

    }

    override fun notifyUserInterfaceForData() {

        runOnUiThread {

            //Foreground
            allUsersAdapter.notifyDataSetChanged()

        }

    }

}
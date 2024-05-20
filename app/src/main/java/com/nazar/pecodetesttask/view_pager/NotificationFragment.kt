package com.nazar.pecodetesttask.view_pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nazar.pecodetesttask.INotificationHandler
import com.nazar.pecodetesttask.INotificationPermissionHandler
import com.nazar.pecodetesttask.IViewPagerHost
import com.nazar.pecodetesttask.databinding.FragmentNotificationBinding


private const val ARG_NUMBER = "ARG_NUMBER"

class NotificationFragment : Fragment() {

    private var fragmentNumber: Int? = null
    private val fragmentPosition: Int
        get() = fragmentNumber!! - 1

    private var _binding: FragmentNotificationBinding? = null
    private val binding: FragmentNotificationBinding
        get() = _binding!!

    private lateinit var notificationHandler: INotificationHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentNumber = it.getInt(ARG_NUMBER)
        }
        savedInstanceState?.let {
            fragmentNumber = it.getInt(ARG_NUMBER)
        }

        notificationHandler = INotificationHandler.Default(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)

        with(binding) {
            numberTextView.text = fragmentNumber.toString()

            val viewPagerHost = activity as? IViewPagerHost
            val permissionHandler = activity as? INotificationPermissionHandler

            goToNextButton.setOnClickListener {
                viewPagerHost?.swipeRight()
            }
            goToPreviousButton.setOnClickListener {
                viewPagerHost?.swipeLeft()
                notificationHandler.cancelAllNotifications(fragmentPosition)
            }
            sendNotificationButton.setOnClickListener {
                permissionHandler?.let {
                    if (permissionHandler.isPermissionGranted)
                        notificationHandler.sendNotification(fragmentPosition)
                    else permissionHandler.requestPermission()
                }
            }

            if (fragmentPosition == 0) {
                goToPreviousButton.visibility = View.GONE
            }
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ARG_NUMBER, fragmentNumber!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(number: Int) =
            NotificationFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_NUMBER, number)
                }
            }
    }
}
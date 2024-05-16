package com.nazar.pecodetesttask.view_pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nazar.pecodetesttask.INotificationHandler
import com.nazar.pecodetesttask.IViewPagerHost
import com.nazar.pecodetesttask.databinding.FragmentNotificationBinding


private const val ARG_NUMBER = "ARG_NUMBER"

class NotificationFragment : Fragment() {

    private var number: Int? = null

    private var _binding: FragmentNotificationBinding? = null
    private val binding: FragmentNotificationBinding
        get() = _binding!!

    private lateinit var notificationHandler: INotificationHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            number = it.getInt(ARG_NUMBER)
        }
        savedInstanceState?.let {
            number = it.getInt(ARG_NUMBER)
        }

        notificationHandler = INotificationHandler.Default(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)

        with(binding) {
            numberTextView.text = number.toString()

            val viewPagerHost = activity as? IViewPagerHost

            goToNextButton.setOnClickListener {
                viewPagerHost?.swipeRight()
            }
            goToPreviousButton.setOnClickListener {
                viewPagerHost?.swipeLeft()
                notificationHandler.cancelAllNotifications(number!!)
            }
            sendNotificationButton.setOnClickListener {
                notificationHandler.sendNotification(number!!)
            }

            if (number == 1) {
                goToPreviousButton.visibility = View.GONE
            }
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ARG_NUMBER, number!!)
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
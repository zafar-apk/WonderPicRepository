package apk.zafar.wonderpic.ui.auth

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import apk.zafar.wonderpic.KEY_AUTHENTICATED
import apk.zafar.wonderpic.R
import apk.zafar.wonderpic.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val viewModel: AuthViewModel by activityViewModels()
    private var preferencesListener: OnSharedPreferenceChangeListener? = null

    @Inject
    lateinit var preferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authProgressBar: ProgressBar = view.findViewById(R.id.auth_progress)

        (requireActivity() as MainActivity)
            .isProgress.observe(viewLifecycleOwner) {
                authProgressBar.isVisible = it
            }

        val signInBtn: Button = view.findViewById(R.id.button_sign_in_vk)

        signInBtn.setOnClickListener {
            viewModel.loginVK(requireActivity())
        }

        preferencesListener = OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key != KEY_AUTHENTICATED) return@OnSharedPreferenceChangeListener
            if (sharedPreferences.getBoolean(KEY_AUTHENTICATED, false)) {
                navigateToHome()
            }
        }

        preferences.registerOnSharedPreferenceChangeListener(preferencesListener)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        preferencesListener?.let {
            preferences.unregisterOnSharedPreferenceChangeListener(it)
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(
            AuthFragmentDirections.actionAuthFragmentToHomeFragment()
        )
    }

}
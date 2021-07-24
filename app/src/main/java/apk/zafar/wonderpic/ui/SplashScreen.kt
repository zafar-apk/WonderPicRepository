package apk.zafar.wonderpic.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import apk.zafar.wonderpic.R
import apk.zafar.wonderpic.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.fragment_splash) {

    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(2000)
            val direction =
                if (viewModel.isAuthorized)
                    SplashScreenDirections.actionSplashScreenToHomeFragment()
                else SplashScreenDirections.actionSplashScreenToAuthFragment()

            findNavController().navigate(direction)
        }
    }
}
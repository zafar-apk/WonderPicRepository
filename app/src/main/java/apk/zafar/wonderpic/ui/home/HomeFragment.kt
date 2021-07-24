package apk.zafar.wonderpic.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import apk.zafar.wonderpic.R
import apk.zafar.wonderpic.ui.MainActivity
import apk.zafar.wonderpic.ui.home.adapter.PhotoAdapter
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private var photoAdapter: PhotoAdapter? = null
    private var loadingProgressBar: ProgressBar? = null
    private val listener = NavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.logout -> showAlertDialog()
        }
        true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView(view)
        setSearchView(view)
        loadCurated()
        processLoading()
        setUserData()

    }

    private fun setUserData() {
        lifecycleScope.launchWhenStarted {
            viewModel.getUser().observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    with(
                        (requireActivity() as MainActivity)
                            .navView
                    ) {
                        menu.findItem(R.id.email)
                            .title = "Email: " + (user.email ?: "Не указан")
                        val headerView = getHeaderView(0)

                        val navUsername: TextView =
                            headerView.findViewById(R.id.user_name)
                        navUsername.text = user.name

                        Glide
                            .with(requireContext())
                            .load(user.photo)
                            .circleCrop()
                            .into(headerView.findViewById(R.id.user_photo))
                    }


                }
            }
        }
        (requireActivity() as MainActivity)
            .navView.setNavigationItemSelectedListener(listener)
    }

    private fun processLoading() {
        photoAdapter?.addLoadStateListener {
            loadingProgressBar?.isVisible = photoAdapter?.snapshot().isNullOrEmpty()
        }
    }

    private fun loadCurated() {
        lifecycleScope.launch {
            viewModel.fetchCurated(requireContext()).collectLatest {
                photoAdapter?.submitData(it)
            }
        }
    }

    private fun setSearchView(view: View) {
        val searchVField: TextInputEditText = view.findViewById(R.id.search_edit_text)
        searchVField.doOnTextChanged { text, _, _, count ->
            lifecycleScope.launch {
                delay(300)
                if (count > 2 && text?.toString() != null) {
                    viewModel.search(text.toString(), requireContext()).collectLatest {
                        photoAdapter?.submitData(it)
                    }
                }
            }
        }
    }

    private fun setRecyclerView(view: View) {
        photoAdapter = PhotoAdapter()
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, VERTICAL)
        loadingProgressBar = view.findViewById(R.id.mainProgressBar)
        val recyclerView: RecyclerView = view.findViewById(R.id.images_recycler_view)

        recyclerView.apply {
            adapter = photoAdapter
            layoutManager = staggeredGridLayoutManager
        }
    }

    private fun showAlertDialog() {
        (requireActivity() as MainActivity).drawerLayout.close()
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Выход")
            .setMessage("Вы действительно хотите выйти?")
            .setPositiveButton("Выйти") { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.logout()

                    withContext(Dispatchers.Main) {
                        findNavController().navigate(R.id.splashScreen)
                    }
                }
            }
            .setNegativeButton("Нет", null)
            .create()
            .show()
    }

}

import com.example.playlistmaker.App
import com.example.playlistmaker.search.data.storage.PrefsStorageClient
import com.example.playlistmaker.search.data.storage.StorageClient
import com.example.playlistmaker.settings.data.repository.ThemeSwitcherRepositoryImpl
import com.example.playlistmaker.settings.domain.api.ThemeSwitcherInteractor
import com.example.playlistmaker.settings.domain.api.ThemeSwitcherRepository
import com.example.playlistmaker.settings.domain.impl.ThemeSwitcherInteractorImpl
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {

    single<StorageClient<Boolean>> {
        PrefsStorageClient<Boolean>(
            prefs = get(),
            dataKey = App.IS_THEME_DARK,
            type = object : TypeToken<Boolean>() {}.type,
            gson = Gson()
        )
    }

    single<ThemeSwitcherRepository> {
        ThemeSwitcherRepositoryImpl(get())
    }

    single<ThemeSwitcherInteractor> {
        ThemeSwitcherInteractorImpl(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }
}
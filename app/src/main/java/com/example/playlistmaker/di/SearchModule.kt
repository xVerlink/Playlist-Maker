import android.content.Context
import com.example.playlistmaker.App
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.TracksApi
import com.example.playlistmaker.search.data.repository.HistoryManagerRepositoryImpl
import com.example.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.search.data.storage.PrefsStorageClient
import com.example.playlistmaker.search.data.storage.StorageClient
import com.example.playlistmaker.search.domain.api.HistoryManagerInteractor
import com.example.playlistmaker.search.domain.api.HistoryManagerRepository
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.impl.HistoryManagerInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchModule = module {

    single<TracksApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(TracksApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<StorageClient<ArrayList<Track>>>(qualifier = named("search")) {
        PrefsStorageClient<ArrayList<Track>>(
            prefs = get(),
            dataKey = App.SEARCH_HISTORY_KEY,
            type = object : TypeToken<ArrayList<Track>>() {}.type,
            gson = get()
        )
    }

    single {
        androidContext()
            .getSharedPreferences(App.PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
    }

    single {
        Gson()
    }

    single<HistoryManagerRepository> {
        HistoryManagerRepositoryImpl(get(qualifier = named("search")), get())
    }

    single<HistoryManagerInteractor> {
        HistoryManagerInteractorImpl(get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }
}
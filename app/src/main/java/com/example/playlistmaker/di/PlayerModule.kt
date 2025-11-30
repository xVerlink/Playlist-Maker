import android.media.MediaPlayer
import com.example.playlistmaker.player.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.api.MediaPlayerRepository
import com.example.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {
    single<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(MediaPlayer())
    }
    single<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }
    viewModel { (trackUrl: String) ->
        PlayerViewModel(
            url = trackUrl,
            playerInteractor = get(),
            favoritesInteractor = get()
        )
    }
}
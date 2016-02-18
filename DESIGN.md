Endpoints:
* Songs
  * GET /songs (returns songs that live in FMAAS_HOME or $HOME/songs)
  * GET /songs/:song (returns the named song. TODO: We filter out songs with spaces, how can we fix?)
* Controls
  * GET /status (what song is playing? how far along is it?)
  * POST /play (body needs to be a song. what song should we play?)
  * POST /stop (stops the currently playing song.)
  * POST /reset (resets moppy? I dunno)
  * POST /repeat (flag for making a song repeat once its finished)
  * GET /moppy (dunno just a plaything at the moment)

Tasks:
* Probably need to rearrange the project structure
* test songs.clj
* Some sort of Configuration settings for Moppy
* Playlists? 

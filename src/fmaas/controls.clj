(ns fmaas.controls
  (:require [fmaas.sequencer :as sequencer]
            [fmaas.songs :as songs]))

(defn status-handler [req]
  {:status 200 :headers {"Content-Type" "application/json"} :body (str (sequencer/get-status))})

(defn play-handler [req]
  (let [song-files (songs/get-song-files)]
    (sequencer/play-song (first song-files)))
  "playing...")

(defn stop-handler [req]
  (sequencer/stop-song)
  "stopped...")

(defn reset-handler [req]
  (str "reset"))

(defn repeat-handler [req]
  (str "repeat"))

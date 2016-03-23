(ns fmaas.controls
  (:require [fmaas.sequencer :as sequencer]
            [fmaas.songs :as songs]))

(def playing nil)

(defn get-playing []
  playing)

(defn set-playing [s]
  (def playing s))

(defn status-handler [req]
  (str "status"))

(defn play-handler [req]
  (let [song-files (songs/get-song-files)]
    ;(println song-files)
    (sequencer/play-song (first song-files))))

(defn stop-handler [req]
  (str "stop"))

(defn reset-handler [req]
  (str "reset"))

(defn repeat-handler [req]
  (str "repeat"))

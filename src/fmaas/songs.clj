(ns fmaas.songs
  (:require [fmaas.config.config :as config]
            [ring.util.response :as response]))

; song-re is a regex pattern that is most characters, except spaces!
;(def song-re #"[A-Za-z0-9_.]*[^\s]")

(defn get-song-name [f] (clojure.string/replace (.getName f) ".mid" ""))

(defn song? [song]
  "Checks a file and ensures it exists and is not a directory"
  (and (.exists song)
       (not (.isDirectory song))))

(defn get-song-files []
  "Filters the files in songs-dir to ensure they are songs and returns them as a sequence"
  (let [songs-dir (:songs-dir config/get-config)]
    (filter song? (file-seq songs-dir))))

(defn get-song-details [sf]
  (let [midi-sequence (javax.sound.midi.MidiSystem/getSequence sf)]
    {:name (get-song-name sf)
     :path (.getAbsolutePath sf)
     :resolution (.getResolution midi-sequence)
     :tracks (count (.getTracks midi-sequence))
     :length (float (/ (.getMicrosecondLength midi-sequence) 1000))}))

(defn find-song [songs song-name]
  "Filters the songs using the given song-name"
  (filter
    #(= song-name (get-song-name %1))
    songs))

(defn get-songs []
  (map get-song-name (get-song-files)))

(defn get-songs-handler [req]
  (response/response (get-songs)))

(defn get-song-handler [req]
  (let [song-file (first (find-song (get-song-files) (:song (:params req))))]
    (if (nil? song-file)
      (response/not-found "Song not found.")
      (response/response (get-song-details song-file)))))

(ns fmaas.songs
  (:require [clojure.java.io :as io]
            [cheshire.core :as json]))

(def songs-dir (or (System/getenv "FMAAS_HOME")
                   (str (System/getenv "HOME") "/songs")))

; song-re is a regex pattern that is most characters, except spaces!
(def song-re #"[A-Za-z0-9_.]*[^\s]")

(defn get-song-name [f] (clojure.string/replace (.getName f) ".mid" ""))

(defn get-song-files [] (filter 
                         (fn [f] (and (.exists f) (not (.isDirectory f)) (re-matches song-re (.getName f))))
                         (file-seq (io/file songs-dir))))

(defn get-song-details [sf]
  (let [midi-sequence (javax.sound.midi.MidiSystem/getSequence sf)]
    {:name (get-song-name sf)
     :path (.getAbsolutePath sf)
     :resolution (.getResolution midi-sequence)
     :tracks (count (.getTracks midi-sequence))
     :length (float (/ (.getMicrosecondLength midi-sequence) 1000))}))

(defn find-first [f cond]
  (->> f
       (filter cond)
       first))

; This might be neat to use?
(defn render-default-response [body]
  {:status 200 :headers {"Content-Type" "application/json"} :body (str body)})

(defn render-error-response [statusCode body]
  {:status statusCode :headers {} :body (str body)})

(defn get-songs []
  (map get-song-name (get-song-files)))

(defn get-songs-handler [req]
  (render-default-response (json/generate-string (get-songs))))

(defn get-song-handler [req]
  (let [song-file (find-first (get-song-files) #(= (get-song-name %) (:song (:params req))))]
    (if (nil? song-file)
      (render-error-response 404 "Song not found.")
      (render-default-response (json/generate-string (get-song-details song-file))))))

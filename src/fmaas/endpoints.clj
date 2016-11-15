(ns fmaas.endpoints
  (:use [compojure.route :only [files not-found]]
        [compojure.handler :only [site]]
        [compojure.core :only [routes GET POST DELETE ANY context]]
        [cheshire.core :refer :all]
        org.httpkit.server)
  (:require [fmaas.songs :as songs]
            [fmaas.controls :as controls])
  (:import [com.moppy/moppydesk.Constants]))

(defn ping-handler [req]
  (str "pong"))

; Using cheshire JSON library
(defn echo-handler [req]
  "I am supposed to parse the request body into a string and return it."
  (generate-string (parse-string (slurp (:body req)))))

(defn moppy-handler [req]
  "I am learning how to call a Java class here."
  (str (moppydesk.Constants/NUM_MIDI_CHANNELS)))

(defn all []
  (context "/api/v1" []
           (routes
            (GET "/ping" [] ping-handler)
            (GET "/songs" [] songs/get-songs-handler)
            (GET "/songs/:song" [song] songs/get-song-handler)
            (GET "/status" [] controls/status-handler)
            (POST "/play" [] controls/play-handler)
            (POST "/stop" [] controls/stop-handler)
            (POST "/reset" [] controls/reset-handler)
            (POST "/repeat" [] controls/repeat-handler)
            (GET "/moppy" [] moppy-handler))))

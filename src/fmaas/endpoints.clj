(ns fmaas.endpoints
  (:use [compojure.route :only [files not-found]]
        [compojure.handler :only [site]]
        [compojure.core :only [routes GET POST DELETE ANY context]]
        org.httpkit.server)
  (:require [fmaas.songs :as songs]
            [fmaas.controls :as controls]))

(defn ping-handler [req]
  (str "pong"))

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
            (POST "/repeat" [] controls/repeat-handler))))

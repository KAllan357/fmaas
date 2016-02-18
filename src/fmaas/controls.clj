(ns fmaas.controls)

(def playing nil)

(defn get-playing []
  playing)

(defn set-playing [s]
  (def playing s))

(defn status-handler [req]
  (str "status"))

(defn play-handler [req]
  (println req)
  (str (slurp (:body req)) " is playing!"))

(defn stop-handler [req]
  (str "stop"))

(defn reset-handler [req]
  (str "reset"))

(defn repeat-handler [req]
  (str "repeat"))

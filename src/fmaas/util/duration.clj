(ns fmaas.util.duration
  (:import (java.time Duration)
           (java.time.temporal ChronoUnit)))

(defn duration-string [micros]
  (let [duration (Duration/of micros ChronoUnit/MICROS)
        minutes (.toMinutes duration)
        seconds (mod (.getSeconds duration) 60)
        nanos (.getNano duration)]
    (format "%d:%d" minutes seconds)))

(defn position-string [position-micros total-micros]
  (let [position (duration-string position-micros)
        total (duration-string total-micros)]
    (str position "/" total)))

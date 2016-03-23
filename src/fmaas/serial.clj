(ns fmaas.serial)

(defn make-connection
  ""
  [config]
  (gnu.io.NRSerialPort. (:comPort config) (:baud config)))

(defn make-serial-connection
  "Makes"
  [config]
  (let [connection (make-connection config)]
    (try
      (.connect connection)
      (if (.isConnected connection)
        connection (println "error not connected"))
      (catch Exception exception
        (println exception)))))


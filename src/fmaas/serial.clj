(ns fmaas.serial
  (:import gnu.io.NRSerialPort))

(defn make-connection
  ""
  [config]
  (NRSerialPort. (:comPort config) (:baud config)))

(defn connect-serial-connection [connection]
  "Attempts to connect to the given connection"
  (try
    (.connect connection)
    (if (.isConnected connection)
      connection
      (throw (Exception. "cannot connect")))
    (catch Exception exception
      (println exception))))

(defn make-serial-connection
  "Makes a serial connection and attempts to connect"
  [config]
  (let [connection (make-connection config)]
    (connect-serial-connection connection)))

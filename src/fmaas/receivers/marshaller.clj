(ns fmaas.receivers.marshaller
  (:require [fmaas.config.config :as config]
            [fmaas.receivers.pass-through :as pass-through]
            [fmaas.serial :as serial]))

(defn parse-receiver-type
  "Returns a reified javax.sound.midi.Receiver given a string type and a serial-connection"
  [type serial-connection]
  (condp = type
    "PassThrough" (pass-through/pass-through serial-connection)
    (str "uh oh?")))

(defn map-receivers [kv]
  (let [serial-connection (serial/make-connection (val kv))]
    (vector (key kv) {:serial-connection serial-connection 
                      :receiver (parse-receiver-type (:type (val kv) serial-connection) serial-connection)})))

; {:COM3 {:serial-connection object[] :receiver object[]}
(defn make-receivers [receivers-conf]
  (into {} (map map-receivers receivers-conf)))

(defn channel-to-keyword [channel]
  (keyword (str channel)))

(defn marshaller-receiver
  "Returns a reified javax.sound.midi.Receiver that uses the current config to map channels to a 
  specialized receiver with connetions to a serial device. Calling close on the Receiver will close
  all marshalled receivers. Calling send will decode the message and call send on the appropriate
  channel."
  []
  ;; get-config reads the config file so only do that once
  (let [conf config/get-config
        receivers (make-receivers (:receivers conf))
        channels-conf (:channels conf)]
    (reify javax.sound.midi.Receiver
      (close [this]
        ; Call close on each receiver
        (doseq [keyval receivers]
          (.close (:receiver (val keyval)))))
      (send [this message timestamp]
        (let [channel-kw (channel-to-keyword (.getChannel message))
              receiver-key (channel-kw channels-conf)
              receiver (:receiver (receiver-key receivers))]
          (.send receiver message timestamp))))))

;(defn close [this] (doseq [keyval my-map] (.close (.getOutputStream (val keyval))) (.disconnect (val keyval))))

;(defn send [this message timestamp] "Get Channel from message" (.write (.getOutputStream (chan my-map)) (.getMessage message)))

; This should be a receivers namespace
;   read config
;     - for :receivers we make a Map {:PassThrough object}
;   read :channels
;     - for each we make a Map {:1 object[]}
;   return a Reciever that can send() close() - both delegate to the 2nd Map in this comment

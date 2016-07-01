(ns fmaas.receivers.pass-through)

(defn pass-through [serial-connection]
  (reify javax.sound.midi.Receiver
    (close [this]
      (.close (.getOutputStream serial-connection))
      (.disconnect serial-connection))
    (send [this message timestamp]
      (.write (.getOutputStream serial-connection) (.getMessage message)))))


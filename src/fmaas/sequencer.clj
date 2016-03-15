(ns fmaas.sequencer
  (:import (javax.sound.midi MidiSystem
                             Sequencer
                             Transmitter
                             Receiver)
           (gnu.io NRSerialPort)))

(defn format-hack [msg]
  (format "Here is the length: %d\n" (.getLength msg)))

(defn format-hack2 [msg]
  (format "Thing: %s\n" (seq (.getMessage msg))))

(def maybe-a-receiver
  (reify Receiver
    (close [this] (println "closing!"))
    (send [this message timestamp] (spit "C:/Users/Kyle/songs/out" 
                                         (format-hack2 message) :append true))))

(def serial-connection
  (new NRSerialPort "COM3" 9600))

(defn get-serial-os [sc]
  (.connect sc)
  (.getOutputStream sc))

(defn who-knows [midiFile]
  (let [sequencer (MidiSystem/getSequencer false) 
        transmitter (.getTransmitter sequencer)
        sequence (MidiSystem/getSequence midiFile)
        r maybe-a-receiver]
    ;(println sequencer)
    ;(println transmitter)
    ;(println sequence)
    (.setSequence sequencer sequence)
    ;(println (.getSequence sequencer))
    (.setReceiver transmitter r)
    (.open sequencer)
    (.start sequencer)
    (Thread/sleep 10000)
    (.stop sequencer)
    (.close sequencer)))

; sequencer = MidiSystem.getSequencer(false)
; sequencer.open()
; transmitter = sequencer.getTransmitter()


; sequence = MidiSystem.getSequence(file)
; sequencer.setSequence(sequence)

; transmitter.setReceiver(receiver)

; sequencer.start()

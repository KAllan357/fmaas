(ns fmaas.sequencer
  (:import (javax.sound.midi MidiSystem
                             Sequencer
                             Transmitter
                             Receiver)))
(def maybe-a-receiver
  (reify Receiver
    (close [this] (println "closing!"))
    (send [this message timestamp] (spit "C:/Users/Kyle/songs/out" (.getMessage message) :append true))))


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

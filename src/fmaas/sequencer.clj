(ns fmaas.sequencer
  (:import (javax.sound.midi MidiSystem
                             Sequencer
                             Transmitter
                             Receiver)
           (gnu.io NRSerialPort))
  (:require [fmaas.serial :as serial]))

(defn play-song [midiFile]
  (let [sequencer (MidiSystem/getSequencer false) 
        transmitter (.getTransmitter sequencer)
        sequence (MidiSystem/getSequence midiFile)]
    (.setSequence sequencer sequence)
    (.setReceiver transmitter (let [connection (serial/make-serial-connection {:comPort "COM3" :baud 9600})]
                                (reify Receiver
                                  (close [this] 
                                    (.close (.getOutputStream connection))
                                    (.disconnect connection))
                                  (send [this message timestamp] 
                                    (.write (.getOutputStream connection) (.getMessage message))))))
    (.open sequencer)
    (.start sequencer)
    (Thread/sleep 10000)
    (.stop sequencer)
    (.close sequencer)))

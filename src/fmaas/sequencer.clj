(ns fmaas.sequencer
  (:import (javax.sound.midi MidiSystem
                             Sequencer
                             Transmitter
                             Receiver)
           (gnu.io NRSerialPort))
  (:require [fmaas.serial :as serial]))

(def system-sequencer (ref (MidiSystem/getSequencer false)))

(defn close-transmitter-and-receiver [transmitter]
  (let [receiver (.getReceiver transmitter)]
    (when-not (nil? receiver)
      (.close receiver))
    (.close transmitter)))

(defn close-and-stop-song [sequencer]
  (let [transmitters (.getTransmitters sequencer)]
    (doseq [t transmitters]
      (close-transmitter-and-receiver t))
    (when (.isOpen sequencer)
      (.stop sequencer)
      (.close sequencer))
    sequencer))

(defn start-song [sequencer sequence]
  (let [transmitter (.getTransmitter sequencer)]
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
    sequencer))

(defn stop-song []
  (dosync
   (let [sequencer @system-sequencer]
     (alter system-sequencer close-and-stop-song))))

(defn play-song [midiFile]
  (dosync
   (let [sequencer @system-sequencer
         sequence (MidiSystem/getSequence midiFile)]
     (stop-song)
     (alter system-sequencer start-song sequence))))

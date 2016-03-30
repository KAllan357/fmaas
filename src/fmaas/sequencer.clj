(ns fmaas.sequencer
  (:import (javax.sound.midi MidiSystem
                             Sequencer
                             Transmitter
                             Receiver)
           (gnu.io NRSerialPort))
  (:require [fmaas.serial :as serial]))

; system-sequencer is a shared reference to the MidiSystem Sequencer
(def system-sequencer (ref (MidiSystem/getSequencer false)))

(defn close-transmitter-and-receiver [transmitter]
  "Obtains a receiver from the given transmitter and closes both"
  (let [receiver (.getReceiver transmitter)]
    (when-not (nil? receiver)
      (.close receiver))
    (.close transmitter)))

(defn close-and-stop-song [sequencer]
  "Closes all transmitters, receivers, and the sequencer"
  (let [transmitters (.getTransmitters sequencer)]
    (doseq [t transmitters]
      (close-transmitter-and-receiver t))
    (when (.isOpen sequencer)
      (.stop sequencer)
      (.close sequencer))
    sequencer))

(defn start-song [sequencer sequence]
  "Starts a song by adding the sequence to the sequencer and creating a Receiver of the serial connection"
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

(defn get-sequence-details [sequence]
  (bean sequence))

(defn get-status []
  (let [sequencer @system-sequencer]
    (if (.isRunning sequencer) 
      (get-sequence-details (.getSequence sequencer))
      {:status "not playing"})))

(defn stop-song [] 
  "Stops a song that's currently playing"
  (dosync
   (let [sequencer @system-sequencer]
     (alter system-sequencer close-and-stop-song))))

(defn play-song [midiFile]
  "Plays a song"
  (dosync
   (let [sequencer @system-sequencer
         sequence (MidiSystem/getSequence midiFile)]
     (stop-song)
     (alter system-sequencer start-song sequence))))

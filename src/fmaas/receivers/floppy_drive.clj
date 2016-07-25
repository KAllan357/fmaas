(ns fmaas.receivers.floppy-drive)

; Code derived from 
; https://github.com/SammyIAm/Moppy/blob/moppy-advanced/Java/MoppyDesk/src/moppydesk/outputs/MoppyPlayerOutput.java

(def micro-periods [
                    0 0 0 0 0 0 0 0 0 0 0 0
                    0 0 0 0 0 0 0 0 0 0 0 0
                    30578 28861 27242 25713 24270 22909 21622 20409 19263 18182 17161 16198 ;C1 - B1
                    15289 14436 13621 12856 12135 11454 10811 10205 9632 9091 8581 8099 ;C2 - B2
                    7645 7218 6811 6428 6068 5727 5406 5103 4816 4546 4291 4050 ;C3 - B3
                    3823 3609 3406 3214 3034 2864 2703 2552 2408 2273 2146 2025 ;C4 - B4
                    0 0 0 0 0 0 0 0 0 0 0 0
                    0 0 0 0 0 0 0 0 0 0 0 0
                    0 0 0 0 0 0 0 0 0 0 0 0
                    ])

(def bend-cents 200)

(def arduino-resolution 40)

; current-period is a muteable collection so maybe this is an atom? tbd...
(def current-period [])

(defn between? [test x y]
  (and (> test x)
       (< test y)))

(defn note-off? [x]
  (between? x 127 144))

(defn note-on? [x]
  (between? x 143 160))

(defn pitch-bend? [x]
  (between? x 223 240))

(defn dispatch-message [message]
  (let [status (.getStatus message)]
    (condp #(%1 %2) status
      note-off? (dispatch-note-off message)
      note-on? "note-on"
      pitch-bend? "pitch-bend"
      nil)))

(defn get-period [byte]
  (let [micro-period-entry (micro-periods (bit-and byte 0xFF))]
    (/ micro-period-entry (* arduino-resolution 2))))

; give me a byte[] with 3 bytes to send to arduino
(defn get-send-event [pin period]
  (byte-array 3 [(byte pin)
                 (byte (-> (bit-shift-right period 8) (bit-and 0xFF)))
                 (byte (bit-and period 0xFF))]))

; write 3 bytes to the output stream; flush
(defn write-event [os pin period]
  (let [event (get-send-event pin period)]
       (doto os 
         (.write event)
         (.flush))))

; Is this too crazy? It returns a function that takes a serial-connection
; so I don't have to pass it through dispatch-message.
(defn dispatch-note-off [message]
  (let [status (.getStatus message)
        pin (-> status (- 127) (* 2))]
    (fn [serial-connection]
      (let [os (.getOutputStream serial-connection)]
        (write-event os pin 0)
        ;() change the atom?
        ))))

(defn dispatch-note-on [message]
  (let [status (.getStatus message)
        pin (-> status (- 143) (* 2))
        period (get-period ((.getMessage message) 1))]
    (fn [serial-connection]
      (let [os (.getOutputStream serial-connection)]
        (if (= ((.getMessage message) 2) 0)
          (do
            (write-event os pin 0)
            ();change the atom?
            )
          (do
            (write-event os pin period)
            () ;change the atom?
            ))))))

(defn floppy-drive [serial-connection]
  (reify javax.sound.midi.Receiver
    (close [this])
    (send [this message timestamp]
      (let [dispatch-fn (dispatch-message message)]
        (when dispatch-fn
          (dispatch-fn serial-connection))))))


(ns fmaas.receivers.marshaller-test
  (:require [clojure.test :refer :all]
            [fmaas.receivers.marshaller :refer :all]))

(deftest test-make-receivers
  (testing "Maps input to a serial connection and a receiver"
    (let [r (make-receivers {:COM3 {:type "PassThrough" :baud 9600 :comPort "COM3"}})]
      (is (contains? r :COM3))
      (is (contains? (r :COM3) :connection))
      (is (contains? (r :COM3) :receiver))
;      (is (= gnu.io.NRSerialPort (type (:connection (:COM3 r)))))
;      (is (instance? javax.sound.midi.Receiver (:receiver (:COM3 r))))
      )))

(deftest test-parse-receiver-type-pass-through
  (testing "A PassThrough type yields a javax.sound.midi.Receiver interface"
    (is (instance? javax.sound.midi.Receiver (parse-receiver-type "PassThrough" "fake thing")))))


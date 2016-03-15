//#include <TimerOne.h>
#include <SoftwareSerial.h>

#define VS1053_RX  2 // This is the pin that connects to the RX pin on VS1053
#define VS1053_RESET 9 // This is the pin that connects to the RESET pin on VS1053
#define RESOLUTION 40 //Microsecond resolution for notes

// See http://www.vlsi.fi/fileadmin/datasheets/vs1053.pdf Pg 31
#define VS1053_BANK_DEFAULT 0x00
#define VS1053_BANK_DRUMS1 0x78
#define VS1053_BANK_DRUMS2 0x7F
#define VS1053_BANK_MELODY 0x79

// See http://www.vlsi.fi/fileadmin/datasheets/vs1053.pdf Pg 32 for more!
#define VS1053_GM1_OCARINA 80

#define MIDI_NOTE_ON  0x90
#define MIDI_NOTE_OFF 0x80
#define MIDI_CHAN_MSG 0xB0
#define MIDI_CHAN_BANK 0x00
#define MIDI_CHAN_VOLUME 0x07
#define MIDI_CHAN_PROGRAM 0xC0

SoftwareSerial VS1053_MIDI(0, 2); // TX only, do not use the 'rx' side

//const byte STATUS_BYTE = B10000000; // 128 0x80
//const byte DATA_BYTE = B01111111; // 127 0x7F

//Setup
void setup(){  
  //Timer1.initialize(RESOLUTION); // Set up a timer at the defined resolution
  //Timer1.attachInterrupt(tick); // Attach the tick function
  Serial.begin(9600);

  VS1053_MIDI.begin(31250); // MIDI uses a 'strange baud rate'
  
  pinMode(VS1053_RESET, OUTPUT);
  digitalWrite(VS1053_RESET, LOW);
  delay(10);
  digitalWrite(VS1053_RESET, HIGH);
  delay(10);

  midiSetChannelBank(0, VS1053_BANK_MELODY);
  midiSetInstrument(0, VS1053_GM1_OCARINA);
  midiSetChannelVolume(0, 127);
}

void loop(){
  if (Serial.available() > 2) {
    byte status_byte = Serial.read();
    byte message1 = Serial.read();

    if (Serial.peek() < 127) {
       byte message2 = Serial.read();
        // STATUS_BYTE
        if (0x80 >= status_byte || status_byte <= 0x8F) {
          // Note Off
          uint8_t chan = status_byte & 0x0F;
          uint8_t note = message1 & 0x7F; 
          uint8_t vel = message2 & 0x7F;
          midiNoteOff(chan, note, vel);
        } else if (0x90 >= status_byte || status_byte <= 0x9F) {
          // Note On
          uint8_t chan = status_byte & 0x0F;
          uint8_t note = message1 & 0x7F;
          uint8_t vel = message2 & 0x7F;
          midiNoteOn(chan, note, vel);
        }
    }

    // 0x80 >= Note Off <= 0x8F
    // 0x90 >= Note On <= 0x9F

    // Read 2 bytes;
    // Make sure byte1 is a Status byte
    // peek a 3rd byte; if its a status byte we don't do anything else we read it
  }
}


// Let's only care about NOTE_ON / NOTE_OFF for now.

void midiNoteOn(uint8_t chan, uint8_t n, uint8_t vel) {
  if (chan > 15) return;
  if (n > 127) return;
  if (vel > 127) return;
  
  VS1053_MIDI.write(MIDI_NOTE_ON | chan);
  VS1053_MIDI.write(n);
  VS1053_MIDI.write(vel);
}

void midiNoteOff(uint8_t chan, uint8_t n, uint8_t vel) {
  if (chan > 15) return;
  if (n > 127) return;
  if (vel > 127) return;
  
  VS1053_MIDI.write(MIDI_NOTE_OFF | chan);
  VS1053_MIDI.write(n);
  VS1053_MIDI.write(vel);
}

void midiSetInstrument(uint8_t chan, uint8_t inst) {
  if (chan > 15) return;
  inst --; // page 32 has instruments starting with 1 not 0 :(
  if (inst > 127) return;
  
  VS1053_MIDI.write(MIDI_CHAN_PROGRAM | chan);  
  VS1053_MIDI.write(inst);
}


void midiSetChannelVolume(uint8_t chan, uint8_t vol) {
  if (chan > 15) return;
  if (vol > 127) return;
  
  VS1053_MIDI.write(MIDI_CHAN_MSG | chan);
  VS1053_MIDI.write(MIDI_CHAN_VOLUME);
  VS1053_MIDI.write(vol);
}

void midiSetChannelBank(uint8_t chan, uint8_t bank) {
  if (chan > 15) return;
  if (bank > 127) return;
  
  VS1053_MIDI.write(MIDI_CHAN_MSG | chan);
  VS1053_MIDI.write((uint8_t)MIDI_CHAN_BANK);
  VS1053_MIDI.write(bank);
}

/*
Called by the timer interrupt at the specified resolution.
 */
//void tick() {
//}

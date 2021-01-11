package cyou.keithhacks.ems;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundEffects {
	
	protected static SoundEffects instance;
	public static SoundEffects get() {
		if (instance == null)
			instance = new SoundEffects();
		return instance;
	}
	
	protected static class AudioData {
		public byte[] buffer;
		public AudioFormat format;
		
		public AudioData(AudioInputStream audio) throws IOException {
			format = audio.getFormat();
			buffer = new byte[(int) (format.getFrameSize() * audio.getFrameLength())];
			
			int offset = 0;
			try {
				while (offset < buffer.length) {
					offset += audio.read(buffer, offset, buffer.length - offset);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			audio.close();
		}
		
		public void openClip(Clip clip) throws LineUnavailableException {
			clip.open(format, buffer, 0, buffer.length);
		}
	}
	
	public AudioData dialogOpen;
	public AudioData dialogClose;
	
	public AudioData open;
	public AudioData close;
	
	public AudioData iconify;
	public AudioData deiconify;
	
	public AudioData maximize;
	public AudioData unmaximize;
	
	public AudioData shade;
	public AudioData unshade;
	
	public AudioData stick;
	public AudioData unstick;
	
	protected SoundEffects() {
		dialogOpen = load(this.getClass().getClassLoader().getResource("cyou/keithhacks/ems/assets/KDE_Dialog_Appear.wav"));
		dialogClose = load(this.getClass().getClassLoader().getResource("cyou/keithhacks/ems/assets/KDE_Dialog_Disappear.wav"));
		
		open = load(this.getClass().getClassLoader().getResource("cyou/keithhacks/ems/assets/KDE_Window_Open.wav"));
		close = load(this.getClass().getClassLoader().getResource("cyou/keithhacks/ems/assets/KDE_Window_Close.wav"));
		
		iconify = load(this.getClass().getClassLoader().getResource("cyou/keithhacks/ems/assets/KDE_Window_Iconify.wav"));
		deiconify = load(this.getClass().getClassLoader().getResource("cyou/keithhacks/ems/assets/KDE_Window_DeIconify.wav"));
		
		maximize = load(this.getClass().getClassLoader().getResource("cyou/keithhacks/ems/assets/KDE_Window_Maximize.wav"));
		unmaximize = load(this.getClass().getClassLoader().getResource("cyou/keithhacks/ems/assets/KDE_Window_UnMaximize.wav"));
		
		shade = load(this.getClass().getClassLoader().getResource("cyou/keithhacks/ems/assets/KDE_Window_Shade_Up.wav"));
		unshade = load(this.getClass().getClassLoader().getResource("cyou/keithhacks/ems/assets/KDE_Window_Shade_Down.wav"));
		
		stick = load(this.getClass().getClassLoader().getResource("cyou/keithhacks/ems/assets/KDE_Window_Sticky.wav"));
		unstick = load(this.getClass().getClassLoader().getResource("cyou/keithhacks/ems/assets/KDE_Window_UnSticky.wav"));
	}
	
	protected AudioData load(URL url) {
		if (url == null)
			return null;
		
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(url);
			return new AudioData(audio);
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void play(AudioData audio) {
		if (audio == null)
			return;
		try {
			Clip clip = AudioSystem.getClip();
			clip.addLineListener(new LineListener() {
				boolean played = false;
				public void update(LineEvent e) {
					// Wait until clip is open to start playback
					if (clip.isOpen() && !played) {
						played = true;
						
						// HACK sleeping 20ms fixes issue where sound sometimes doesn't play
						try {
							Thread.sleep(20);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						
						clip.start();
					}
					
					// Free resources
					if (e.getType() == LineEvent.Type.STOP && clip.isOpen()) {
						clip.close();
					}
				}
			});
			audio.openClip(clip);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}

package com.xplus.commons.pattern.behavioral.command.audioplay;

/**
 * 具体命令角色类：倒带
 *
 */
public class RewindCommand implements Command {

  private AudioPlayer audioPlayer;
  
  public RewindCommand(AudioPlayer audioPlayer) {
    this.audioPlayer = audioPlayer;
  }
  
  @Override
  public void execute() {
    this.audioPlayer.rewind();
  }

}

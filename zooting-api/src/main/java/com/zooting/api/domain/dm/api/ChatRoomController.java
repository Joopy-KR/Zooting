// 프론트 테스트를 위한 임시 컨트롤러
package com.zooting.api.domain.dm.api;

import com.zooting.api.domain.dm.application.DMService;
import com.zooting.api.domain.dm.dto.ChatRoom;
import com.zooting.api.domain.dm.dto.DMDto;
import com.zooting.api.domain.dm.entity.DMRoom;
import com.zooting.api.domain.dm.service.ChatService;
import com.zooting.api.domain.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;
    private final DMService dmService;
    private final MemberService memberService;

    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model) {
        return "/chat/room";
    }
    // 모든 채팅방 목록 반환
//    @GetMapping("/rooms")
//    @ResponseBody
//    public List<ChatRoom> room() {
//        return chatService.findAllRoom();
//    }
    @GetMapping("/rooms")
    @ResponseBody
    public List<DMDto> room(@RequestParam(value = "sender") String sender) {
        List<DMDto> dmDtos = new ArrayList<>();
        log.info("sender {}",sender);
        List<DMRoom> dmRooms = memberService.getDmRooms(sender);
        for(DMRoom dmRoom: dmRooms){
            log.info("dmRoomId {} {} {}", dmRoom.getId(), dmRoom.getSender().getEmail(), dmRoom.getReceiver().getEmail());
            DMDto newdmDto = new DMDto(dmRoom.getId(),null,null,dmRoom.getSender().getEmail(),dmRoom.getReceiver().getEmail(),null);
            dmDtos.add(newdmDto);
        }
        List<DMRoom> dmRoomsReverse = memberService.getDmRoomsReverse(sender);
        for(DMRoom dmRoom: dmRoomsReverse){
            log.info("dmRoomId {} {} {}", dmRoom.getId(), dmRoom.getSender().getEmail(), dmRoom.getReceiver().getEmail());
            DMDto newdmDto = new DMDto(dmRoom.getId(),null,null, dmRoom.getReceiver().getEmail(),dmRoom.getSender().getEmail(),null);
            dmDtos.add(newdmDto);
        }
        return dmDtos;
    }
    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam(value = "name") String name) {
        return chatService.createRoom(name);
    }
    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

    @GetMapping("/subtest")
    public String subTest() {
//        model.addAttribute("roomId", roomId);
        return "/chat/subtest";
    }
    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatService.findById(roomId);
    }

}


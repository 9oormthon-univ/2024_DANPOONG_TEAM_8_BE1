package groom.Buddy_BE.character;

import groom.Buddy_BE.member.Member;
import groom.Buddy_BE.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final MemberService memberService;

    @Transactional
    // 캐릭터 생성
    public Character createCharacter(Character.CharacterType characterType, String characterName) {

        // 새로운 캐릭터 생성 및 설정
        Character character = new Character();
        character.setCharacterType(characterType);
        character.setCharacterName(characterName);
        character.setLevel(1);

        // 캐릭터 저장 후 멤버에 설정
        characterRepository.save(character);

        return character;
    }



    //캐릭터 레벨 증가
    public Character levelUpCharacter(Long kakaoId) {
        // 1. kakaoId로 Member 조회
        Member member = memberService.findByKakaoId(kakaoId);
        if (member == null) {
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }

        // 2. Member의 Character 확인
        Character character = member.getCharacter();
        if (character == null) {
            throw new IllegalArgumentException("회원에게 캐릭터가 존재하지 않습니다.");
        }

        // 3. 캐릭터의 레벨 증가
        character.setLevel(character.getLevel() + 1);

        characterRepository.save(character);

        // 4. 변경된 캐릭터 저장
        return character;
    }
}
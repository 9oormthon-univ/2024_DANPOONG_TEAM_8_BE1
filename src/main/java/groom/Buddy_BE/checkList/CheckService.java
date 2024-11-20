package groom.Buddy_BE.checkList;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckService {

    private final CheckListRepository checkListRepository;

    // 사전 점검표 결과 생성
    public CheckList createCheckList(int fistQ, int secondQ, int thirdQ, int fourthQ, int fifthQ, int sixthQ, int seventhQ, int eighthQ) {
        CheckList checkList = new CheckList();
        checkList.setFistQ(fistQ);
        checkList.setSecondQ(secondQ);
        checkList.setThirdQ(thirdQ);
        checkList.setFourthQ(fourthQ);
        checkList.setFifthQ(fifthQ);
        checkList.setSixthQ(sixthQ);
        checkList.setSeventhQ(seventhQ);
        checkList.setEighthQ(eighthQ);

        return checkList;
    }

    // 사전 점검표 저장
    @Transactional
    public CheckList saveCheckList(CheckList checkList) {
        return checkListRepository.save(checkList);
    }
}

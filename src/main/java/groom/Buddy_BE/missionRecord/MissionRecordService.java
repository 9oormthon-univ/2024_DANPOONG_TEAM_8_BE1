package groom.Buddy_BE.missionRecord;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionRecordService {

    private final MissionRecordRepository missionRecordRepository;

    //미션 기록 생성
    public MissionRecord createMissionRecord(String content,String feedback){
        MissionRecord missionRecord = new MissionRecord();
        missionRecord.setContent(content);
        missionRecord.setFeedback(feedback);

        return missionRecordRepository.save(missionRecord);
    }
}

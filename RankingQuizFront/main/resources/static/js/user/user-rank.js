// 점수에 따라 랭크 이미지 결정
function setRankImage(pointValue) {
    const rankImage = document.getElementById('rankImage');

    if (pointValue >= 1000) {
        rankImage.src = '/image/rank/6Diamond.webp'; // 골드 랭크 이미지
    } else if (pointValue >= 800) {
        rankImage.src = '/image/rank/5Platinum.webp'; // 실버 랭크 이미지
    } else if (pointValue >= 600) {
        rankImage.src = '/image/rank/4Gold.webp'; // 브론즈 랭크 이미지
    } else if (pointValue >= 400) {
        rankImage.src = '/image/rank/3Silver.webp';
    } else if (pointValue >= 200) {
        rankImage.src = '/image/rank/2Bronze.webp';
    } else {
        rankImage.src = '/image/rank/1Iron.webp';
    }

}

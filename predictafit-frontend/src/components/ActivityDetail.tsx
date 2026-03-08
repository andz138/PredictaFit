import { useEffect, useState } from "react"
import { useParams } from "react-router-dom"
import { getActivityById, getActivityDetail } from "../services/api"
import { Box, Card, CardContent, Divider, Typography } from "@mui/material"

type Activity = {
  activityId: string
  userId: string
  activityType: string
  durationMinutes: number
  caloriesBurned: number
  startedAt: string
}

type Recommendation = {
  overallRecommendation: string
  improvementAreas?: string[]
  suggestions?: string[]
  safetyNotes?: string[]
}

const ActivityDetail = () => {
  const { id } = useParams()

  const [activity, setActivity] = useState<Activity | null>(null)
  const [recommendation, setRecommendation] = useState<Recommendation | null>(null)

  useEffect(() => {
    const fetchActivityDetail = async (activityId: string) => {
      try {
        const activityResponse = await getActivityById(activityId)
        setActivity(activityResponse.data)

        try {
          const recommendationResponse = await getActivityDetail(activityId)
          setRecommendation(recommendationResponse.data)
        } catch {
          setRecommendation(null)
        }

      } catch (error) {
        console.error("Failed to fetch activity detail:", error)
      }
    }

    if (id) {
      fetchActivityDetail(id)
    }
  }, [id])

  if (!activity) {
    return <Typography>Loading...</Typography>
  }

  return (
    <Box sx={{ maxWidth: 800, mx: "auto", p: 2 }}>
      {/* Activity Card */}
      <Card sx={{ mb: 2 }}>
        <CardContent>
          <Typography variant="h5" gutterBottom>
            Activity Details
          </Typography>

          <Typography>
            Type: {activity.activityType}
          </Typography>

          <Typography>
            Duration: {activity.durationMinutes} minutes
          </Typography>

          <Typography>
            Calories Burned: {activity.caloriesBurned}
          </Typography>

          <Typography>
            Date: {new Date(activity.startedAt).toLocaleString()}
          </Typography>
        </CardContent>
      </Card>

      {/* Recommendation Card */}
      {recommendation && (
        <Card>
          <CardContent>

            <Typography variant="h5" gutterBottom>
              AI Recommendation
            </Typography>

            {/* Analysis */}
            <Typography variant="h6">Analysis</Typography>
            <Typography>
              {recommendation.overallRecommendation}
            </Typography>

            {/* Improvements */}
            {recommendation.improvementAreas && recommendation.improvementAreas.length > 0 && (
              <>
                <Divider sx={{ my: 2 }} />

                <Typography variant="h6">
                  Improvements
                </Typography>

                {recommendation.improvementAreas.map((improvement, index) => (
                  <Typography key={index}>
                    • {improvement}
                  </Typography>
                ))}
              </>
            )}

            {/* Suggestions */}
            {recommendation.suggestions && recommendation.suggestions.length > 0 && (
              <>
                <Divider sx={{ my: 2 }} />

                <Typography variant="h6">
                  Suggestions
                </Typography>

                {recommendation.suggestions.map((suggestion, index) => (
                  <Typography key={index}>
                    • {suggestion}
                  </Typography>
                ))}
              </>
            )}

            {/* Safety */}
            {recommendation.safetyNotes && recommendation.safetyNotes.length > 0 && (
              <>
                <Divider sx={{ my: 2 }} />

                <Typography variant="h6">
                  Safety Guidelines
                </Typography>

                {recommendation.safetyNotes.map((note, index) => (
                  <Typography key={index}>
                    • {note}
                  </Typography>
                ))}
              </>
            )}

          </CardContent>
        </Card>
      )}
    </Box>
  )
}

export default ActivityDetail